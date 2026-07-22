"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import styles from "./AppNav.module.css";

const links = [
  { href: "/upload", label: "Carga CSV" },
  { href: "/process", label: "Procesar" },
  { href: "/quarantine", label: "Cuarentena" },
];

export function AppNav() {
  const pathname = usePathname();

  return (
    <header className={styles.header}>
      <div className={styles.brand}>
        <span className={styles.mark}>iR</span>
        <div>
          <p className={styles.title}>i-Route</p>
          <p className={styles.subtitle}>Gestión de comercios</p>
        </div>
      </div>
      <nav className={styles.nav}>
        {links.map((link) => {
          const active = pathname === link.href;
          return (
            <Link
              key={link.href}
              href={link.href}
              className={active ? `${styles.link} ${styles.active}` : styles.link}
            >
              {link.label}
            </Link>
          );
        })}
      </nav>
    </header>
  );
}
