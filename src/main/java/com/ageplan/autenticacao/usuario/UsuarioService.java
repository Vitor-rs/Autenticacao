package com.ageplan.autenticacao.usuario;

import com.ageplan.autenticacao.config.exceptions.ResourceNotFoundException;
import com.ageplan.autenticacao.papel.Papel;
import com.ageplan.autenticacao.papel.PapelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável por gerenciar as operações relacionadas à entidade {@link Usuario}.
 * Implementa a interface {@link UserDetailsService} para fornecer detalhes do usuário para autenticação.
 */
@Service
public class UsuarioService implements UserDetailsService {

    private static final String MENSAGEM = "Usuário não encontrado com id: ";

    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Construtor que injeta as dependências necessárias.
     *
     * @param usuarioRepository o repositório de usuários
     * @param papelRepository   o repositório de papéis
     * @param passwordEncoder   o codificador de senhas
     */
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PapelRepository papelRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.papelRepository = papelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Carrega um usuário pelo nome de usuário.
     *
     * @param username o nome de usuário
     * @return os detalhes do usuário
     * @throws UsernameNotFoundException se o usuário não for encontrado
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByNomeUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * @param usuarioDTO o DTO contendo os dados do usuário a ser criado
     * @return o DTO do usuário criado
     */
    @Transactional
    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
        validarEmailENomeUsuario(usuarioDTO.getEmail(), usuarioDTO.getNomeUsuario());

        Usuario usuario = new Usuario();
        updateUsuarioFromDTO(usuario, usuarioDTO);
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return convertToDTO(savedUsuario);
    }

    /**
     * Busca um usuário pelo seu identificador.
     *
     * @param id o identificador do usuário
     * @return o DTO do usuário encontrado
     * @throws ResourceNotFoundException se o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public UsuarioDTO getUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MENSAGEM + id));
        return convertToDTO(usuario);
    }

    /**
     * Busca um usuário pelo nome de usuário.
     *
     * @param username o nome de usuário
     * @return o DTO do usuário encontrado
     * @throws UsernameNotFoundException se o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public UsuarioDTO getUsuarioByUsername(String username) {
        Usuario usuario = usuarioRepository.findByNomeUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
        return convertToDTO(usuario);
    }

    /**
     * Busca todos os usuários cadastrados no sistema com paginação.
     *
     * @param pageable o objeto de paginação
     * @return uma página de DTOs dos usuários encontrados
     */
    @Transactional(readOnly = true)
    public Page<UsuarioDTO> getAllUsuarios(Pageable pageable) {
        Page<Usuario> page = usuarioRepository.findAll(pageable);
        return page.map(this::convertToDTO);
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id         o identificador do usuário a ser atualizado
     * @param usuarioDTO o DTO contendo os novos dados do usuário
     * @return o DTO do usuário atualizado
     * @throws ResourceNotFoundException se o usuário não for encontrado
     */
    @Transactional
    public UsuarioDTO update(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MENSAGEM + id));

        validarEmailENomeUsuarioParaAtualizacao(usuarioDTO.getEmail(), usuarioDTO.getNomeUsuario(), id);

        updateUsuarioFromDTO(usuario, usuarioDTO);
        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return convertToDTO(updatedUsuario);
    }

    /**
     * Deleta um usuário pelo seu identificador.
     *
     * @param id o identificador do usuário a ser deletado
     * @throws ResourceNotFoundException se o usuário não for encontrado
     */
    @Transactional
    public void deleteUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException(MENSAGEM + id);
        }
        usuarioRepository.deleteById(id);
    }

    /**
     * Registra um novo usuário no sistema com papel padrão.
     *
     * @param usuarioDTO o DTO contendo os dados do usuário a ser registrado
     * @return o DTO do usuário registrado
     */
    @Transactional
    public UsuarioDTO registrarUsuario(UsuarioDTO usuarioDTO) {
        validarEmailENomeUsuario(usuarioDTO.getEmail(), usuarioDTO.getNomeUsuario());

        Usuario usuario = new Usuario();
        updateUsuarioFromDTO(usuario, usuarioDTO);
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        Papel papelAluno = papelRepository.findByNomePapel(Papel.NomePapel.ALUNO)
                .orElseThrow(() -> new ResourceNotFoundException("Papel ALUNO não encontrado"));
        if (usuario.getPapeis() == null) {
            usuario.setPapeis(new ArrayList<>());
        }
        if (!usuario.getPapeis().contains(papelAluno)) {
            usuario.getPapeis().add(papelAluno);
        }

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return convertToDTO(savedUsuario);
    }

    /**
     * Valida se o email e o nome de usuário já estão em uso.
     *
     * @param email       o email a ser validado
     * @param nomeUsuario o nome de usuário a ser validado
     * @throws IllegalArgumentException se o email ou nome de usuário já estiverem em uso
     */
    private void validarEmailENomeUsuario(String email, String nomeUsuario) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já está em uso");
        }
        if (usuarioRepository.existsByNomeUsuario(nomeUsuario)) {
            throw new IllegalArgumentException("Nome de usuário já está em uso");
        }
    }

    /**
     * Valida se o email e o nome de usuário já estão em uso por outro usuário.
     *
     * @param email       o email a ser validado
     * @param nomeUsuario o nome de usuário a ser validado
     * @param id          o identificador do usuário atual
     * @throws IllegalArgumentException se o email ou nome de usuário já estiverem em uso por outro usuário
     */
    private void validarEmailENomeUsuarioParaAtualizacao(String email, String nomeUsuario, Long id) {
        if (usuarioRepository.existsByEmailAndIdNot(email, id)) {
            throw new IllegalArgumentException("Email já está em uso por outro usuário");
        }
        if (usuarioRepository.existsByNomeUsuarioAndIdNot(nomeUsuario, id)) {
            throw new IllegalArgumentException("Nome de usuário já está em uso por outro usuário");
        }
    }

    /**
     * Atualiza os dados de um usuário a partir de um DTO.
     *
     * @param usuario    a entidade usuário a ser atualizada
     * @param usuarioDTO o DTO contendo os novos dados do usuário
     */
    private void updateUsuarioFromDTO(Usuario usuario, UsuarioDTO usuarioDTO) {
        usuario.setNomeCompleto(usuarioDTO.getNomeCompleto());
        usuario.setNomeUsuario(usuarioDTO.getNomeUsuario());
        usuario.setEmail(usuarioDTO.getEmail());
        if (usuarioDTO.getPapeis() != null) {
            List<Papel> list = new ArrayList<>();
            for (Papel.NomePapel nomePapel : usuarioDTO.getPapeis()) {
                Papel papel = papelRepository.findByNomePapel(nomePapel).orElseThrow(() -> new ResourceNotFoundException("Papel não encontrado: " + nomePapel));
                list.add(papel);
            }
            usuario.setPapeis(list);
        }
    }

    /**
     * Converte uma entidade {@link Usuario} para um DTO {@link UsuarioDTO}.
     *
     * @param usuario a entidade usuário
     * @return o DTO do usuário
     */
    private UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNomeCompleto(usuario.getNomeCompleto());
        dto.setNomeUsuario(usuario.getNomeUsuario());
        dto.setEmail(usuario.getEmail());
        dto.setPapeis(usuario.getPapeis().stream()
                .map(Papel::getNomePapel)
                .collect(Collectors.toSet()));
        return dto;
    }
}