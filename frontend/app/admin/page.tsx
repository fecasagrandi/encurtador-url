"use client";

import { useState, useEffect } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import {
  Link2,
  Trash2,
  Copy,
  Check,
  BarChart3,
  ExternalLink,
  LogOut,
} from "lucide-react";
import {
  listarUrls,
  obterEstatisticas,
  deletarUrl,
  encurtarUrl,
  UrlResponse,
  EstatisticasResponse,
} from "@/lib/api";
import { useToast } from "@/lib/useToast";
import Button from "@/components/Button";
import Card from "@/components/Card";
import Input from "@/components/Input";
import Toast from "@/components/Toast";
import ThemeToggle from "@/components/ThemeToggle";

export default function AdminPage() {
  const router = useRouter();
  const [authenticated, setAuthenticated] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [urls, setUrls] = useState<UrlResponse[]>([]);
  const [stats, setStats] = useState<EstatisticasResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [copiedId, setCopiedId] = useState<number | null>(null);
  const [urlOriginal, setUrlOriginal] = useState("");
  const [encurtando, setEncurtando] = useState(false);
  const [urlCriada, setUrlCriada] = useState<string | null>(null);
  const { toast, showToast, hideToast } = useToast();

  useEffect(() => {
    const credentials = localStorage.getItem("credentials");
    if (credentials) {
      const decoded = atob(credentials);
      const [user, pass] = decoded.split(":");
      setUsername(user);
      setPassword(pass);
      carregarDados(user, pass);
    } else {
      setLoading(false);
      router.push("/login");
    }
  }, [router]);

  const carregarDados = async (user: string, pass: string) => {
    try {
      const [urlsData, statsData] = await Promise.all([
        listarUrls(user, pass),
        obterEstatisticas(user, pass),
      ]);
      setUrls(urlsData);
      setStats(statsData);
      setAuthenticated(true);
      setLoading(false);
    } catch (err) {
      console.error("Erro ao carregar dados:", err);
      localStorage.removeItem("credentials");
      localStorage.removeItem("usuario");
      setLoading(false);
      router.push("/login");
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Tem certeza que deseja deletar esta URL?")) return;

    try {
      await deletarUrl(id, username, password);
      setUrls(urls.filter((url) => url.id !== id));
      if (stats) {
        setStats({
          ...stats,
          totalUrls: stats.totalUrls - 1,
        });
      }
    } catch (err) {
      showToast("Erro ao deletar URL", "error");
    }
  };

  const handleCopy = async (urlCurta: string, id: number) => {
    try {
      await navigator.clipboard.writeText(urlCurta);
      setCopiedId(id);
      setTimeout(() => setCopiedId(null), 2000);
    } catch (err) {
      console.error("Erro ao copiar:", err);
    }
  };

  const handleEncurtar = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!urlOriginal.trim()) return;

    setEncurtando(true);
    setUrlCriada(null);

    try {
      const resultado = await encurtarUrl({ urlOriginal }, username, password);
      setUrlCriada(resultado.urlCurta);
      setUrlOriginal("");

      await carregarDados(username, password);
    } catch (err) {
      console.error("Erro ao encurtar URL:", err);
      showToast("Erro ao encurtar URL. Verifique se a URL é válida.", "error");
    } finally {
      setEncurtando(false);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("credentials");
    localStorage.removeItem("usuario");
    setAuthenticated(false);
    setUsername("");
    setPassword("");
    setUrls([]);
    setStats(null);
    router.push("/login");
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 dark:from-gray-900 dark:to-gray-800 flex items-center justify-center">
        <div className="text-center">
          <div className="w-16 h-16 border-4 border-blue-600 dark:border-blue-400 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-gray-600 dark:text-gray-400">Carregando...</p>
        </div>
      </div>
    );
  }

  if (!authenticated) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 dark:from-gray-900 dark:to-gray-800 flex items-center justify-center">
        <div className="text-center">
          <p className="text-gray-600 dark:text-gray-400">Redirecionando para login...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 dark:from-gray-900 dark:to-gray-800">
      <nav className="bg-white dark:bg-gray-800 shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex justify-between items-center">
          <div className="flex items-center gap-2">
            <Link2 className="w-6 h-6 text-blue-600 dark:text-blue-400" />
            <h1 className="text-xl font-bold text-gray-900 dark:text-white">Dashboard Admin</h1>
          </div>
          <div className="flex gap-2 items-center">
            <ThemeToggle />
            <Link href="/">
              <Button variant="secondary">Home</Button>
            </Link>
            <Button
              variant="secondary"
              onClick={handleLogout}
              className="flex items-center gap-2"
            >
              <LogOut className="w-4 h-4" />
              Sair
            </Button>
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <Card className="mb-8">
          <h2 className="text-xl font-bold text-gray-900 mb-4">
            Encurtar Nova URL
          </h2>
          <form onSubmit={handleEncurtar} className="space-y-4">
            <div>
              <label
                htmlFor="urlOriginal"
                className="block text-sm font-medium text-gray-700 mb-2"
              >
                URL Original
              </label>
              <Input
                id="urlOriginal"
                type="url"
                value={urlOriginal}
                onChange={(e) => setUrlOriginal(e.target.value)}
                placeholder="https://exemplo.com/sua-url-longa"
                required
                disabled={encurtando}
              />
            </div>

            <Button type="submit" disabled={encurtando}>
              {encurtando ? "Encurtando..." : "Encurtar URL"}
            </Button>

            {urlCriada && (
              <div className="p-4 bg-green-50 border border-green-200 rounded-lg">
                <p className="text-sm text-green-800 font-medium mb-2">
                  ✅ URL encurtada com sucesso!
                </p>
                <div className="flex items-center gap-2">
                  <Input value={urlCriada} readOnly className="flex-1" />
                  <Button
                    onClick={() => {
                      navigator.clipboard.writeText(urlCriada);
                      showToast("URL copiada!", "success");
                    }}
                    variant="secondary"
                  >
                    <Copy className="w-4 h-4" />
                  </Button>
                </div>
              </div>
            )}
          </form>
        </Card>

        {stats && (
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            <Card>
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-gray-600 mb-1">Total de URLs</p>
                  <p className="text-3xl font-bold text-gray-900">
                    {stats.totalUrls}
                  </p>
                </div>
                <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
                  <Link2 className="w-6 h-6 text-blue-600" />
                </div>
              </div>
            </Card>

            <Card>
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-gray-600 mb-1">Total de Acessos</p>
                  <p className="text-3xl font-bold text-gray-900">
                    {stats.totalAcessos}
                  </p>
                </div>
                <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
                  <BarChart3 className="w-6 h-6 text-green-600" />
                </div>
              </div>
            </Card>

            <Card>
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-gray-600 mb-1">Mais Acessada</p>
                  <p className="text-3xl font-bold text-gray-900">
                    {stats.acessosMaisAcessada}
                  </p>
                  <p className="text-xs text-gray-500">cliques</p>
                </div>
                <div className="w-12 h-12 bg-purple-100 rounded-full flex items-center justify-center">
                  <ExternalLink className="w-6 h-6 text-purple-600" />
                </div>
              </div>
            </Card>
          </div>
        )}

        <Card>
          <h2 className="text-xl font-bold text-gray-900 mb-4">Suas URLs</h2>

          {urls.length === 0 ? (
            <p className="text-center text-gray-500 py-8">
              Nenhuma URL encurtada ainda
            </p>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-gray-200">
                    <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">
                      URL Original
                    </th>
                    <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">
                      Código
                    </th>
                    <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">
                      Acessos
                    </th>
                    <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">
                      Ações
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {urls.map((url) => (
                    <tr
                      key={url.id}
                      className="border-b border-gray-100 hover:bg-gray-50"
                    >
                      <td className="py-3 px-4">
                        <p
                          className="text-sm text-gray-900 truncate max-w-md"
                          title={url.urlOriginal}
                        >
                          {url.urlOriginal}
                        </p>
                        <p className="text-xs text-gray-500">
                          {new Date(url.criadoEm).toLocaleDateString("pt-BR")}
                        </p>
                      </td>
                      <td className="py-3 px-4">
                        <code className="text-sm text-gray-900 bg-gray-100 px-2 py-1 rounded font-mono">
                          {url.codigoCurto}
                        </code>
                      </td>
                      <td className="py-3 px-4 text-center">
                        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                          {url.acessos}
                        </span>
                      </td>
                      <td className="py-3 px-4">
                        <div className="flex items-center justify-center gap-2">
                          <button
                            onClick={() => handleCopy(url.urlCurta, url.id)}
                            className="p-2 text-gray-600 hover:text-blue-600 transition-colors"
                            title="Copiar URL"
                          >
                            {copiedId === url.id ? (
                              <Check className="w-4 h-4 text-green-600" />
                            ) : (
                              <Copy className="w-4 h-4" />
                            )}
                          </button>
                          <a
                            href={url.urlCurta}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="p-2 text-gray-600 hover:text-blue-600 transition-colors"
                            title="Abrir URL"
                          >
                            <ExternalLink className="w-4 h-4" />
                          </a>
                          <button
                            onClick={() => handleDelete(url.id)}
                            className="p-2 text-gray-600 hover:text-red-600 transition-colors"
                            title="Deletar"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </Card>
      </main>

      <Toast
        message={toast.message}
        type={toast.type}
        isVisible={toast.isVisible}
        onClose={hideToast}
      />
    </div>
  );
}
