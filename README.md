# Encurtador de URL - MVP

Projeto acadêmico de um encurtador de URL minimalista desenvolvido com Spring Boot e React.

## 🎯 Objetivo

Desenvolver um MVP que exercite boas práticas modernas de desenvolvimento usando Spring Framework, atendendo aos requisitos de Spring Boot, Spring Data JPA e Spring Security.

## 🔧 Tecnologias

### Backend
- Java 21
- Spring Boot 3.5.5
- Spring Data JPA
- Spring Security (Basic Auth + Cadastro de Usuários)
- PostgreSQL
- Maven

### Frontend
- React 19
- Next.js 15
- TypeScript
- TailwindCSS
- Axios

## 📋 Funcionalidades

### ✅ Implementadas
- **Encurtamento de URLs**: Cria código curto de 6 caracteres para URLs longas
- **Redirecionamento**: Redireciona via `/{codigo}` para URL original
- **Métricas**: Registra total de cliques e último acesso por URL
- **Sistema de Usuários**:
  - Cadastro de novos usuários com senha criptografada (BCrypt)
  - Login com autenticação
  - URLs vinculadas ao usuário criador
- **Endpoints Administrativos**: 
  - Listar URLs do usuário logado
  - Ver estatísticas individuais
  - Deletar URLs próprias
  - Criar novas URLs encurtadas
- **Segurança**: Spring Security com Basic Auth e UserDetailsService customizado
- **Validação**: Validação de URLs e dados de usuário
- **CORS**: Configurado para aceitar requisições do frontend
- **Frontend Completo**:
  - Página inicial de encurtamento
  - Sistema de cadastro e login
  - Dashboard administrativo com estatísticas
  - Gerenciamento de URLs

## 🚀 Como Executar

### Pré-requisitos
- Java 21
- Maven
- PostgreSQL
- Node.js 18+ e npm
- IntelliJ IDEA (recomendado)

### Configuração do Banco de Dados

1. Certifique-se de que o PostgreSQL está rodando
2. As configurações estão em `src/main/resources/application.properties`
3. O banco será criado automaticamente pelo Hibernate

### Executando o Backend

1. Abra o projeto no IntelliJ IDEA
2. Execute a classe `EncurtadorDeUrlApplication`
3. A aplicação estará disponível em `http://localhost:8080`

### Executando o Frontend

1. Navegue até a pasta `frontend`:
   ```bash
   cd frontend
   ```

2. Instale as dependências:
   ```bash
   npm install
   ```

3. Inicie o servidor de desenvolvimento:
   ```bash
   npm run dev
   ```

4. Acesse `http://localhost:3000`

## 📡 Endpoints da API

### Públicos

#### Encurtar URL
```http
POST /api/encurtar
Content-Type: application/json

{
  "urlOriginal": "https://exemplo.com/url-muito-longa"
}
```

**Resposta:**
```json
{
  "urlCurta": "http://localhost:8080/abc123",
  "codigoCurto": "abc123",
  "urlOriginal": "https://exemplo.com/url-muito-longa"
}
```

#### Redirecionar
```http
GET /{codigo}
```
Redireciona para a URL original e incrementa contador de acessos.

### Administrativos (requer autenticação)

**Autenticação:**
- Faça cadastro em `/api/usuarios/cadastro`
- Faça login em `/api/usuarios/login`
- Use as credenciais para acessar endpoints protegidos

#### Listar URLs
```http
GET /api/admin/urls
Authorization: Basic YWRtaW46MTIzNDU2
```

**Resposta:**
```json
[
  {
    "id": 1,
    "urlOriginal": "https://exemplo.com",
    "codigoCurto": "abc123",
    "urlCurta": "http://localhost:8080/abc123",
    "acessos": 42,
    "criadoEm": "2025-11-01T14:30:00"
  }
]
```

#### Obter Estatísticas
```http
GET /api/admin/estatisticas
Authorization: Basic YWRtaW46MTIzNDU2
```

**Resposta:**
```json
{
  "totalUrls": 10,
  "totalAcessos": 150,
  "urlMaisAcessada": 1,
  "acessosMaisAcessada": 42
}
```

#### Deletar URL
```http
DELETE /api/admin/urls/{id}
Authorization: Basic YWRtaW46MTIzNDU2
```

## 🏗️ Estrutura do Projeto

```
src/main/java/br/com/casagrandi/encurtador/
├── api/
│   ├── SaudeController.java
│   └── UrlController.java
├── config/
│   ├── CorsConfig.java
│   └── SecurityConfig.java
├── dto/
│   ├── EncurtarRequest.java
│   ├── EncurtarResponse.java
│   ├── EstatisticasResponse.java
│   └── UrlResponse.java
├── exception/
│   └── GlobalExceptionHandler.java
├── model/
│   ├── AcessoUrl.java
│   ├── Url.java
│   └── Usuario.java
├── repository/
│   ├── UrlRepository.java
│   └── UsuarioRepository.java
├── service/
│   └── UrlService.java
└── EncurtadorDeUrlApplication.java
```

## 📅 Cronograma de Entregas

- ✅ **1ª Etapa (31/08/2025)**: Proposta de projeto
- ✅ **2ª Etapa (30/09/2025)**: MVP com criação e redirecionamento
- ✅ **3ª Etapa (31/10/2025)**: Endpoints administrativos + segurança + sistema de usuários
- 🔄 **4ª Etapa (25/11/2025)**: Frontend completo + documentação final + vídeo

## 👨‍💻 Autor

Felipe Casagrandi

## 📝 Licença

Projeto acadêmico - Uso educacional
