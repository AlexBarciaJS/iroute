import type { Metadata } from "next";
import { AppNav } from "@/components/AppNav";
import "./globals.css";

export const metadata: Metadata = {
  title: "i-Route | Gestión de comercios",
  description: "Carga, procesamiento y cuarentena de comercios",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="es">
      <body>
        <AppNav />
        <main>{children}</main>
      </body>
    </html>
  );
}
