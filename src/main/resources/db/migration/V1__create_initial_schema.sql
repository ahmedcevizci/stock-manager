-- Products
CREATE TABLE tbl_product
(
  fld_name VARCHAR(255) NOT NULL CONSTRAINT pkey_product PRIMARY KEY,
  fld_date_created TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
  --can have different stocks in different locations in future so that's why I normalized it as separate table.
  --some other properties in the future
);

CREATE INDEX IF NOT EXISTS idx_fld_date_created ON tbl_product (fld_date_created);


-- Stocks
CREATE TABLE tbl_stock
(
  fld_uuid UUID NOT NULL CONSTRAINT pkey_stock PRIMARY KEY,
  fld_product_id VARCHAR(255) NOT NULL REFERENCES tbl_product (fld_name),
  fld_quantity INTEGER NOT NULL,
  fld_date_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
  -- location can be added in future.
);

CREATE INDEX IF NOT EXISTS idx_fld_product_id ON tbl_stock (fld_product_id);
CREATE INDEX IF NOT EXISTS idx_fld_quantity ON tbl_stock (fld_quantity);
CREATE INDEX IF NOT EXISTS idx_fld_date_updated ON tbl_stock (fld_date_updated);


-- should be insert only!
-- Stock Events
CREATE TABLE tbl_stock_event
(
  fld_uuid UUID NOT NULL CONSTRAINT pkey_stock_event PRIMARY KEY,
  fld_stock_id UUID NOT NULL REFERENCES tbl_stock (fld_uuid),
  fld_quantity_change INTEGER NOT NULL,
  fld_date_changed TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_fld_stock_id ON tbl_stock_event (fld_stock_id);
CREATE INDEX IF NOT EXISTS idx_fld_quantity_change ON tbl_stock_event (fld_quantity_change);
CREATE INDEX IF NOT EXISTS idx_fld_date_changed ON tbl_stock_event (fld_date_changed);
