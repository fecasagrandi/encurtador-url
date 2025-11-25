import axios from "axios";

const api = axios.create({
  baseURL: "https://encurtador-backend.kaizenapp.com.br",
  headers: {
    "Content-Type": "application/json",
  },
});

export interface EncurtarRequest {
  urlOriginal: string;
}

export interface EncurtarResponse {
  urlCurta: string;
  codigoCurto: string;
  urlOriginal: string;
}

export interface UrlResponse {
  id: number;
  urlOriginal: string;
  codigoCurto: string;
  urlCurta: string;
  acessos: number;
  criadoEm: string;
}

export interface EstatisticasResponse {
  totalUrls: number;
  totalAcessos: number;
  urlMaisAcessada: number | null;
  acessosMaisAcessada: number;
}

export interface UsuarioRequest {
  nomeUsuario: string;
  email: string;
  senha: string;
}

export interface LoginRequest {
  nomeUsuario: string;
  senha: string;
}

export interface UsuarioResponse {
  id: number;
  nomeUsuario: string;
  email: string;
  criadoEm: string;
}

/**
 * Encurta URL publicamente (sem login)
 * Qualquer pessoa pode usar, mas não terá histórico
 */
export const encurtarUrlPublico = async (
  data: EncurtarRequest
): Promise<EncurtarResponse> => {
  const response = await api.post<EncurtarResponse>(
    "/api/encurtar/publico",
    data
  );
  return response.data;
};

/**
 * Encurta URL com autenticação (usuário logado)
 * URL fica vinculada ao usuário com histórico e estatísticas
 */
export const encurtarUrl = async (
  data: EncurtarRequest,
  username: string,
  password: string
): Promise<EncurtarResponse> => {
  const response = await api.post<EncurtarResponse>("/api/encurtar", data, {
    auth: { username, password },
  });
  return response.data;
};

export const listarUrls = async (
  username: string,
  password: string
): Promise<UrlResponse[]> => {
  const response = await api.get<UrlResponse[]>("/api/admin/urls", {
    auth: {
      username,
      password,
    },
  });
  return response.data;
};

export const obterEstatisticas = async (
  username: string,
  password: string
): Promise<EstatisticasResponse> => {
  const response = await api.get<EstatisticasResponse>(
    "/api/admin/estatisticas",
    {
      auth: {
        username,
        password,
      },
    }
  );
  return response.data;
};

export const deletarUrl = async (
  id: number,
  username: string,
  password: string
): Promise<void> => {
  await api.delete(`/api/admin/urls/${id}`, {
    auth: {
      username,
      password,
    },
  });
};

export const cadastrarUsuario = async (
  dados: UsuarioRequest
): Promise<UsuarioResponse> => {
  const response = await api.post<UsuarioResponse>(
    "/api/usuarios/cadastro",
    dados
  );
  return response.data;
};

export const loginUsuario = async (
  dados: LoginRequest
): Promise<UsuarioResponse> => {
  const response = await api.post<UsuarioResponse>(
    "/api/usuarios/login",
    dados
  );
  return response.data;
};

export default api;
