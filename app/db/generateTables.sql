drop TABLE Dia;
drop TABLE Ejercicio;
drop TABLE Serie;

CREATE TABLE Dia (
    id_dia INTEGER PRIMARY KEY
                   NOT NULL,
    nombre TEXT    NOT NULL
);

CREATE TABLE Ejercicio (
    id       INTEGER NOT NULL,
    id_dia   INTEGER REFERENCES Dia (id_dia) 
                     NOT NULL,
    nombre   TEXT    NOT NULL,
    n_series TEXT    NOT NULL,
    PRIMARY KEY (
        id,
        id_dia
    )
);

CREATE TABLE Serie (
    id           INTEGER NOT NULL,
    id_ejercicio INTEGER NOT NULL,
    id_dia       INTEGER NOT NULL,
    peso         TEXT,
    PRIMARY KEY (
        id,
        id_ejercicio,
        id_dia
    ),
    FOREIGN KEY (
        id_ejercicio,
        id_dia
    )
    REFERENCES Ejercicio (id,
    id_dia) 
);