DROP TABLE IF EXISTS tblUsers;
DROP TABLE IF EXISTS tblMonitoredPages;
DROP TABLE IF EXISTS tblNotificationRequests;
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
	fdProcessFlag INT not null
);

CREATE TABLE tblNotificationRequests(
	FK_USER_ID INT NOT NULL,
	FK_PAGE_ID INT NOT NULL,
	fdActive INT NOT NULL
);

CREATE TABLE tblModificationLog(
	FK_PAGE_ID INT NOT NULL,
	fdUpdateTime TEXT NOT NULL
);

PRAGMA journal_mode=WAL;