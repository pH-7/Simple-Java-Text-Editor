# Collaboration using Proteus

## How to run it
> java -Demail=[email] -Dpassword=[password]
-Dconv=[conversation] -jar editor.jar

- `email` - is you Wire email
- `password` - Wire password
- `conversation` - Conversation ID in which to share this doc

## How to obtain conversation ID
Internal Wrapper:
`wire.app.repository.conversation.active_conversation().id`
