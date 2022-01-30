CREATE DATABASE test_emails;

use test_emails;

CREATE TABLE Emails (
	[mailTo] [varchar](320) NOT NULL,
	[subject] [nvarchar](100) NULL,
	[text] [nvarchar](500) NULL,
	[attachment] [varbinary](max) NULL,
	[extension] [varchar](15) NULL,
	[status] [int] NOT NULL,
	[timesend] [datetime] NULL,
	[id] [int] NOT NULL IDENTITY(1,1)  PRIMARY KEY,
)