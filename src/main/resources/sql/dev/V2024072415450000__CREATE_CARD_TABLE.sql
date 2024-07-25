-- Table: public.card

-- DROP TABLE IF EXISTS public.card;

CREATE TABLE IF NOT EXISTS public.card
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    card_number bigint,
    card_type character varying(255) COLLATE pg_catalog."default",
    daily_limit bigint,
    is_active boolean,
    is_blocked boolean,
    monthly_limit bigint,
    user_id bigint,
    CONSTRAINT card_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.card
    OWNER to postgres;