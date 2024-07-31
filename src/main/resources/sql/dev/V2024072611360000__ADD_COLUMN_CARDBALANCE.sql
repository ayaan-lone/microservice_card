ALTER TABLE public.card
    ADD COLUMN IF NOT EXISTS card_balance bigint;