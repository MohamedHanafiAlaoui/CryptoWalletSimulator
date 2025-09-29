CREATE TYPE transaction_status AS ENUM ('PENDING','CONFIRMED','REJECTED');
CREATE TYPE fee_priority AS ENUM ('ECONOMIC','STANDARD','FAST');
CREATE TYPE crypto_type AS ENUM ('BITCOIN','ETHEREUM');



-- wallets table
CREATE TABLE wallets (
  id VARCHAR(50) PRIMARY KEY,
  address VARCHAR(128) UNIQUE NOT NULL,
  crypto_type crypto_type NOT NULL,
  balance NUMERIC(30, 18) NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT now(),
  version VARCHAR(64),    -- bitcoin specific (nullable)
  network VARCHAR(64),    -- bitcoin specific (nullable)
  chain_id INTEGER        -- ethereum specific (nullable)
);

-- transactions table
CREATE TABLE transactions (
  id VARCHAR(50) PRIMARY KEY,
  source_address VARCHAR(128) NOT NULL,
  destination_address VARCHAR(128) NOT NULL,
  amount NUMERIC(30, 18) NOT NULL,
  fees NUMERIC(30, 18) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT now(),
  status transaction_status NOT NULL DEFAULT 'PENDING',
  priority fee_priority NOT NULL DEFAULT 'STANDARD',
  crypto_type crypto_type NOT NULL,
  gas_limit VARCHAR(64),
  gas_price VARCHAR(64),
  size_bytes VARCHAR(64),
  satoshi_per_byte VARCHAR(64)
);

CREATE TABLE mempool (
  id SERIAL PRIMARY KEY,
  transaction_id VARCHAR(50) NOT NULL REFERENCES transactions(id) ON DELETE CASCADE,
  added_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_transactions_source ON transactions(source_address);
CREATE INDEX idx_transactions_dest ON transactions(destination_address);
CREATE INDEX idx_transactions_fees_created ON transactions(fees DESC, created_at DESC);
