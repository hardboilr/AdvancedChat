#Sprint 1 7. sep - 9. sep  

#Implement server (chat via telnet!)

--> Client init --> ----
<-- ask user for username
--> USER#{NAME} --> add user to list 

--> MSG#{receivers)#{message} --> receive message
<-- MSG#{sender)#{message} <-- send message back to receivers

--> STOP# --> remove user from list (user logs out)

#Sprint 2 10. - 13. sep

#Implement GUI

#Elements

TextField for username (only displayed when not logged in)
Connect-button (only displayed when not logged in)

TextArea for sent/received messages
TextField for text input
Send-button to send text
Log-out button

If not connected --> 
<-- ask to submit username
--> change UI accordingly (set a flag)

<-- USERLIST#{NAME1,NAME2,...} <-- server sends an updated list of users right after new client has logged in/logged out
