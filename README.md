# PrivChat - Built for Privacy
A simple end-to-end chatting application enabling users to communicate directly with once another via communication to a central server. Built in Java using Swing, Sockets, etc.

## Features
The app currently enables users to:
* Configure and set up the chatting server itself
* See a list of all active chat participants
* Configure their own chat window's colors and fonts
* Send private messages to other users via the @ sign

## Running the app
To run the app, simply configure the ChatServer.java file, run it, and then have all users run the ChatView.java file in order to connect to the server.

Immediately after connecting to the chat, the following window should become visible:
<img src = connection_view.png>

As seen in the picture above, all messages will be separated by dashes. Additionally, all active users can be seen on the side of the screen.

Finally, colors of the window can be changed via dropdown menu, and all system fonts are available as choices for the chat itself.
<img src = font_color_demo.png>

## Concepts Learned
While building out this application, I learned the fundamentals of polymorphism, inheritance, sockets, servers, networking, and java UI with swing. It also taught me the basics of how messaging applications like Facebook Messenger or Whatsapp work.
