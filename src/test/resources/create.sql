CREATE TABLE profile (
                         email varchar(255) NOT NULL,
                         company_name varchar(255) NOT NULL,
                         legal_name varchar(255) NOT NULL,
                         business_address varchar(255) NOT NULL,
                         legal_address varchar(255) NOT NULL,
                         tax_identifiers varchar(255) NOT NULL,
                         website varchar(255),
                         PRIMARY KEY (email)
);

CREATE TABLE product (
                         ID int NOT NULL AUTO_INCREMENT,
                         product_name varchar(255) NOT NULL,
                         PRIMARY KEY (ID)
);
CREATE TABLE product_subscriptions (
                                       email varchar(255) NOT NULL,
                                       product_id int NOT NULL,
                                       CONSTRAINT product_subscriptions_fk PRIMARY KEY (email, product_id),
                                       FOREIGN KEY (email) REFERENCES profile(email) ON DELETE CASCADE ON UPDATE CASCADE,
                                       FOREIGN KEY (product_id) REFERENCES product(ID) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE product_validation (
                                    profile_id varchar(255) NOT NULL,
                                    product_id int NOT NULL,
                                    status varchar(255) NOT NULL,
                                    CONSTRAINT product_validation_fk PRIMARY KEY (profile_id, product_id),
                                    FOREIGN KEY (product_id) REFERENCES product(ID) ON DELETE CASCADE ON UPDATE CASCADE,
                                    FOREIGN KEY (profile_id) REFERENCES profile(email) ON DELETE CASCADE ON UPDATE CASCADE
);