# i-Route

Aplicación web para carga, procesamiento y cuarentena de comercios.

## Stack

- **Frontend:** React 19 + Next.js 15
- **Backend:** Java 21 + Spring Boot 4
- **Base de datos:** MySQL 8 (scripts + stored procedures)

## Estructura

```
iroute/
├── backend/          # API REST Spring Boot
├── frontend/         # App Next.js
├── database/         # Schema y stored procedures
├── samples/          # CSV de ejemplo
├── docker-compose.yml
└── requerimientos/   # PDF del enunciado
```

## Formato del CSV

Archivo: `commerce_DDMMYYYY.csv`

| Columna         | Descripción              |
|-----------------|--------------------------|
| pc_codcom       | Código del comercio      |
| pc_nomcomred    | Nombre reducido          |
| pc_tipdoc       | Tipo de documento        |
| pc_numdoc       | Número de documento      |
| pc_processdate  | Fecha de proceso (YYYY-MM-DD) |

Ejemplo incluido: `samples/commerce_22072026.csv`

## API REST

| Método | Endpoint                     | Descripción |
|--------|------------------------------|-------------|
| POST   | `/api/commerce/upload`       | Recibe el CSV (`multipart/form-data`, campo `file`). Valida que no esté vacío e inserta con `sp_create_commerce`. |
| POST   | `/api/commerce/process`      | Body: `{ "processDate": "YYYY-MM-DD" }`. Valida y mueve inválidos a cuarentena. Retorna cantidad insertada. |
| GET    | `/api/commerce/quarantine`   | Lista registros de `commerce_quarantine` con `motivo`. |

### Validaciones del proceso

1. `pc_nomcomred` no debe estar vacío.
2. `pc_numdoc` no debe estar vacío, ni contener letras ni caracteres especiales (solo dígitos).

Los inválidos se eliminan de `commerce` y se registran en `commerce_quarantine` con el motivo correspondiente.

## Arranque

### 1. MySQL

Con Docker:

```bash
docker compose up -d
```

Sin Docker, crea la BD y ejecuta:

```bash
mysql -u root -p < database/01_schema.sql
mysql -u root -p < database/02_procedures.sql
```

Credenciales por defecto del `docker-compose.yml`:

- DB: `iroutedb`
- User: `iroute` / Password: `iroute`
- Puerto: `3306`

### 2. Backend

Requiere **JDK 21** (no solo JRE).

```bash
chmod +x scripts/run-backend.sh
./scripts/run-backend.sh
```

O manualmente:

```bash
cd backend
export JAVA_HOME=/ruta/a/jdk-21
./mvnw spring-boot:run
```

API en `http://localhost:8080`

### 3. Frontend

```bash
chmod +x scripts/run-frontend.sh
./scripts/run-frontend.sh
```

O:

```bash
cd frontend
npm install
npm run dev
```

UI en `http://localhost:3000`

## Flujo recomendado de prueba

1. Abrir **Carga CSV** y seleccionar `samples/commerce_22072026.csv`.
2. Previsualizar y enviar al backend.
3. Ir a **Procesar**, usar fecha `2026-07-22` y ejecutar.
4. Revisar en **Cuarentena** los registros con motivo.
