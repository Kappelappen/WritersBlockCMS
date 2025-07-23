-- Skapa category-tabellen

CREATE TABLE category (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE IF NOT EXISTS article (

    id INTEGER PRIMARY KEY AUTOINCREMENT,
	title TEXT NOT NULL,
	description TEXT DEFAULT NULL,
	author TEXT DEFAULT NULL,
	category TEXT DEFAULT NULL,
	status TEXT DEFAULT NULL,
	publish_date TEXT DEFAULT NULL,
	created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
	
);

CREATE TABLE IF NOT EXISTS photo (
	
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	article_id INTEGER NOT NULL,
	image_path TEXT NOT NULL,
	FOREIGN KEY(article_id) REFERENCES article(id)

);

CREATE TABLE IF NOT EXISTS story (
	
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	article_id INTEGER NOT NULL,
	article_text TEXT NOT NULL,
	status TEXT DEFAULT NULL,
	published_at DATE DEFAULT NULL,
	FOREIGN KEY(article_id) REFERENCES article(id)

);

CREATE TABLE IF NOT EXISTS article_status (

    id INTEGER PRIMARY KEY AUTOINCREMENT,
    status_name TEXT DEFAULT NULL

);

INSERT INTO article_status (status_name) VALUES ('Draft');
INSERT INTO article_status (status_name) VALUES ('In review');
INSERT INTO article_status (status_name) VALUES ('Pending approval');
INSERT INTO article_status (status_name) VALUES ('Published');
INSERT INTO article_status (status_name) VALUES ('Scheduled');
INSERT INTO article_status (status_name) VALUES ('Archived');
INSERT INTO article_status (status_name) VALUES ('Rejected');

INSERT INTO category (name, description) VALUES ('All Articles', 'Everything');
INSERT INTO category (name, description) VALUES ('Drafts', 'Work in progress');

INSERT INTO article (title, description, author, category, status) VALUES ("Welcome Post","This is a description","Johan","All articles", "Published");
INSERT INTO article (title, description, author, category, status) VALUES ("Another Draft","This is a description","Kalle","Drafts", "Published");

INSERT INTO story (article_id,article_text) VALUES (1, "Hello world!");
INSERT INTO story (article_id,article_text) VALUES (2, "This is not ready yet.");



ALTER TABLE category ADD COLUMN parent_id INTEGER REFERENCES category(id);

