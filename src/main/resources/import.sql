-- Inserir dados na tabela Usuario
INSERT INTO usuario (id, nome_completo, nome_usuario, email, senha, account_non_expired, account_non_locked, credentials_non_expired, enabled)
VALUES (1, 'Admin', 'admin', 'admin@example.com', 'senha123', true, true, true, true);
INSERT INTO usuario (id, nome_completo, nome_usuario, email, senha, account_non_expired, account_non_locked, credentials_non_expired, enabled)
VALUES (2, 'Professor', 'professor', 'professor@example.com', 'senha123', true, true, true, true);
INSERT INTO usuario (id, nome_completo, nome_usuario, email, senha, account_non_expired, account_non_locked, credentials_non_expired, enabled)
VALUES (3, 'Aluno', 'aluno', 'aluno@example.com', 'senha123', true, true, true, true);

-- Inserir dados na tabela Usuario_Papel
INSERT INTO usuario_papel (usuario_id, papel_id) VALUES (1, 1);
INSERT INTO usuario_papel (usuario_id, papel_id) VALUES (2, 2);
INSERT INTO usuario_papel (usuario_id, papel_id) VALUES (3, 3);