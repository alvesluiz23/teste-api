CREATE DATABASE cadastroVeiculos;


\c cadastroVeiculos;


CREATE TABLE public.veiculos (
    id SERIAL PRIMARY KEY,
    veiculo VARCHAR(255) NOT NULL,
    marca VARCHAR(255) NOT NULL,
    ano INT NOT NULL,
    cor VARCHAR(100) NOT NULL,
    descricao VARCHAR(500),
    vendido BOOLEAN DEFAULT FALSE,
    created DATE DEFAULT CURRENT_DATE,
    updated DATE DEFAULT CURRENT_DATE
);