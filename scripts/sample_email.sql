USE [test_emails]
GO

INSERT INTO [dbo].[Emails]
           ([mailTo]
           ,[subject]
           ,[text]
           ,[attachment]
           ,[extension]
           ,[status]
           ,[timesend]
           ,[id])
     VALUES
           ('barechev@gmail.com',
           'Make українська Great Again',
           'Давайте спілкуватися українською мовою!'
           ,(SELECT * FROM OPENROWSET(BULK 'N:\Projectss\Univer\Java\jsp_pages\тест.docx', SINGLE_BLOB) as import),
           'docx',
           0,
           null,
           1)
GO
