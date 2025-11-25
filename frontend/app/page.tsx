"use client";

import { useState } from "react";
import Link from "next/link";
import { Link2, Copy, Check, ExternalLink, Sparkles } from "lucide-react";
import { encurtarUrlPublico } from "@/lib/api";
import { validateUrlInput, formatUrl } from "@/lib/validation";
import { useToast } from "@/lib/useToast";
import Button from "@/components/Button";
import Input from "@/components/Input";
import Card from "@/components/Card";
import Toast from "@/components/Toast";

export default function Home() {
  const [urlOriginal, setUrlOriginal] = useState("");
  const [urlCurta, setUrlCurta] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [copied, setCopied] = useState(false);
  const { toast, showToast, hideToast } = useToast();

  const handleEncurtar = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setUrlCurta("");
    setCopied(false);

    const validation = validateUrlInput(urlOriginal);
    if (!validation.isValid) {
      setError(validation.error!);
      return;
    }

    setLoading(true);

    try {
      const formattedUrl = formatUrl(urlOriginal.trim());
      const response = await encurtarUrlPublico({ urlOriginal: formattedUrl });
      setUrlCurta(response.urlCurta);
      showToast("URL encurtada com sucesso!", "success");
    } catch (err: any) {
      const errorMsg =
        err.response?.data?.urlOriginal || "Erro ao encurtar URL";
      setError(errorMsg);
      showToast(errorMsg, "error");
    } finally {
      setLoading(false);
    }
  };

  const handleCopy = async () => {
    try {
      await navigator.clipboard.writeText(urlCurta);
      setCopied(true);
      showToast("URL copiada para a área de transferência!", "success");
      setTimeout(() => setCopied(false), 2000);
    } catch (err) {
      console.error("Erro ao copiar:", err);
      showToast("Erro ao copiar URL", "error");
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <nav className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex justify-between items-center">
          <div className="flex items-center gap-2">
            <Link2 className="w-6 h-6 text-blue-600" />
            <h1 className="text-xl font-bold text-gray-900">
              Encurtador de URL
            </h1>
          </div>
          <div className="flex gap-2">
            <Link href="/login">
              <Button variant="secondary">Entrar</Button>
            </Link>
            <Link href="/cadastro">
              <Button>Cadastrar</Button>
            </Link>
          </div>
        </div>
      </nav>

      <main className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
        <div className="text-center mb-12">
          <div className="flex items-center justify-center gap-2 mb-4">
            <Sparkles className="w-8 h-8 text-blue-600 animate-pulse" />
            <h2 className="text-4xl font-bold text-gray-900">
              Encurte suas URLs
            </h2>
            <Sparkles className="w-8 h-8 text-blue-600 animate-pulse" />
          </div>
          <p className="text-lg text-gray-600 max-w-2xl mx-auto">
            Transforme URLs longas em links curtos e fáceis de compartilhar.
            Gerencie, monitore e analise seus links com facilidade.
          </p>
        </div>

        <Card>
          <form onSubmit={handleEncurtar} className="space-y-4">
            <div>
              <label
                htmlFor="url"
                className="block text-sm font-medium text-gray-700 mb-2"
              >
                Cole sua URL longa aqui
              </label>
              <Input
                id="url"
                type="text"
                placeholder="https://exemplo.com/sua-url-muito-longa"
                value={urlOriginal}
                onChange={(e) => setUrlOriginal(e.target.value)}
                disabled={loading}
              />
            </div>

            {error && (
              <div className="p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
                {error}
              </div>
            )}

            <Button type="submit" disabled={loading} className="w-full">
              {loading ? "Encurtando..." : "Encurtar URL"}
            </Button>
          </form>

          {urlCurta && (
            <div className="mt-6 p-4 bg-gradient-to-r from-green-50 to-blue-50 border border-green-200 rounded-lg animate-in slide-in-from-bottom-2">
              <p className="text-sm font-medium text-gray-700 mb-3 flex items-center gap-2">
                <Check className="w-4 h-4 text-green-600" />
                URL encurtada com sucesso!
              </p>
              <div className="flex items-center gap-2">
                <Input
                  type="text"
                  value={urlCurta}
                  readOnly
                  className="flex-1 bg-white"
                />
                <Button
                  onClick={handleCopy}
                  variant="secondary"
                  className="flex items-center gap-2 transition-all hover:scale-105"
                >
                  {copied ? (
                    <>
                      <Check className="w-4 h-4 text-green-600" />
                      Copiado!
                    </>
                  ) : (
                    <>
                      <Copy className="w-4 h-4" />
                      Copiar
                    </>
                  )}
                </Button>
                <a href={urlCurta} target="_blank" rel="noopener noreferrer">
                  <Button
                    variant="secondary"
                    className="transition-all hover:scale-105"
                  >
                    <ExternalLink className="w-4 h-4" />
                  </Button>
                </a>
              </div>
            </div>
          )}
        </Card>

        <div className="mt-12 grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card className="text-center">
            <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <Link2 className="w-6 h-6 text-blue-600" />
            </div>
            <h3 className="font-semibold text-gray-900 mb-2">
              Rápido e Simples
            </h3>
            <p className="text-sm text-gray-600">Encurte URLs em segundos</p>
          </Card>

          <Card className="text-center">
            <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <Check className="w-6 h-6 text-green-600" />
            </div>
            <h3 className="font-semibold text-gray-900 mb-2">Confiável</h3>
            <p className="text-sm text-gray-600">Links sempre disponíveis</p>
          </Card>

          <Card className="text-center">
            <div className="w-12 h-12 bg-purple-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <ExternalLink className="w-6 h-6 text-purple-600" />
            </div>
            <h3 className="font-semibold text-gray-900 mb-2">Rastreável</h3>
            <p className="text-sm text-gray-600">Acompanhe cliques e acessos</p>
          </Card>
        </div>
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
