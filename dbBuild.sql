DROP TABLE IF EXISTS tblUsers;
DROP TABLE IF EXISTS tblMonitoredPages;
DROP TABLE IF EXISTS tblNotificationRequests;
DROP TABLE IF EXISTS tblPageElements;
DROP TABLE IF EXISTS tblModificationLog;

CREATE TABLE tblUsers(
	pk_id INT PRIMARY KEY not null,
	fdEmailAddress TEXT not null
);

CREATE TABLE tblMonitoredPages(
	pk_id INT PRIMARY KEY not null,
	fdURL TEXT not null,
	fdHash BLOB,
	fdLastUpdate TEXT,
	fdName TEXT null,
	fdParseErrorCount INT
);

CREATE TABLE tblNotificationRequests(
	FK_USER_ID INT NOT NULL,
	FK_PAGE_ID INT NOT NULL,
	fdNotificationType TEXT NOT NULL,
	FK_PAGE_ELEMENT_ID INT NULL
);

CREATE TABLE tblPageElements(
	pk_id INT PRIMARY KEY not null,
	FK_PAGE_ID INT NOT NULL,
	fdElementName TEXT,
	fdElementHash BLOB
);

CREATE TABLE tblModificationLog(
	FK_PAGE_ID INT NOT NULL,
	fdUpdateTime TEXT NOT NULL
);

PRAGMA journal_mode=WAL;