'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { Link2, Trash2, Copy, Check, BarChart3, ExternalLink, LogOut } from 'lucide-react';
import { listarUrls, obterEstatisticas, deletarUrl, UrlResponse, EstatisticasResponse } from '@/lib/api';
import Button from '@/components/Button';
import Card from '@/components/Card';

export default function AdminPage() {
  const [authenticated, setAuthenticated] = useState(false);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [urls, setUrls] = useState<UrlResponse[]>([]);
  const [stats, setStats] = useState<EstatisticasResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [copiedId, setCopiedId] = useState<number | null>(null);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const [urlsData, statsData] = await Promise.all([
        listarUrls(username, password),
        obterEstatisticas(username, password),
      ]);
      setUrls(urlsData);
      setStats(statsData);
      setAuthenticated(true);
    } catch (err: any) {
      setError('Credenciais inválidas');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm('Tem certeza que deseja deletar esta URL?')) return;

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
      alert('Erro ao deletar URL');
    }
  };

  const handleCopy = async (urlCurta: string, id: number) => {
    try {
      await navigator.clipboard.writeText(urlCurta);
      setCopiedId(id);
      setTimeout(() => setCopiedId(null), 2000);
    } catch (err) {
      console.error('Erro ao copiar:', err);
    }
  };

  const handleLogout = () => {
    setAuthenticated(false);
    setUsername('');
    setPassword('');
    setUrls([]);
    setStats(null);
  };

  if (!authenticated) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
        <Card className="w-full max-w-md">
          <div className="text-center mb-6">
            <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <Link2 className="w-8 h-8 text-blue-600" />
            </div>
            <h1 className="text-2xl font-bold text-gray-900 mb-2">Dashboard Admin</h1>
            <p className="text-gray-600">Faça login para acessar</p>
          </div>

          <form onSubmit={handleLogin} className="space-y-4">
            <div>
              <label htmlFor="username" className="block text-sm font-medium text-gray-700 mb-2">
                Usuário
              </label>
              <input
                id="username"
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-900 placeholder:text-gray-400"
                required
              />
            </div>

            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-2">
                Senha
              </label>
              <input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 text-gray-900 placeholder:text-gray-400"
                required
              />
            </div>

            {error && (
              <div className="p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
                {error}
              </div>
            )}

            <Button type="submit" disabled={loading} className="w-full">
              {loading ? 'Entrando...' : 'Entrar'}
            </Button>

            <Link href="/">
              <Button variant="secondary" className="w-full">
                Voltar para Home
              </Button>
            </Link>
          </form>
        </Card>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <nav className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex justify-between items-center">
          <div className="flex items-center gap-2">
            <Link2 className="w-6 h-6 text-blue-600" />
            <h1 className="text-xl font-bold text-gray-900">Dashboard Admin</h1>
          </div>
          <div className="flex gap-2">
            <Link href="/">
              <Button variant="secondary">Home</Button>
            </Link>
            <Button variant="secondary" onClick={handleLogout} className="flex items-center gap-2">
              <LogOut className="w-4 h-4" />
              Sair
            </Button>
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {stats && (
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            <Card>
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-gray-600 mb-1">Total de URLs</p>
                  <p className="text-3xl font-bold text-gray-900">{stats.totalUrls}</p>
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
                  <p className="text-3xl font-bold text-gray-900">{stats.totalAcessos}</p>
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
                  <p className="text-3xl font-bold text-gray-900">{stats.acessosMaisAcessada}</p>
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
            <p className="text-center text-gray-500 py-8">Nenhuma URL encurtada ainda</p>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-gray-200">
                    <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">URL Original</th>
                    <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Código</th>
                    <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">Acessos</th>
                    <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700">Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {urls.map((url) => (
                    <tr key={url.id} className="border-b border-gray-100 hover:bg-gray-50">
                      <td className="py-3 px-4">
                        <p className="text-sm text-gray-900 truncate max-w-md" title={url.urlOriginal}>
                          {url.urlOriginal}
                        </p>
                        <p className="text-xs text-gray-500">
                          {new Date(url.criadoEm).toLocaleDateString('pt-BR')}
                        </p>
                      </td>
                      <td className="py-3 px-4">
                        <code className="text-sm bg-gray-100 px-2 py-1 rounded">
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
    </div>
  );
}
