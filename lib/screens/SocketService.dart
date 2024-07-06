import 'dart:convert';
import 'dart:io';

class SocketService {
  late Socket _socket;

  Future<void> connect() async {
    try {
      _socket = await Socket.connect('127.0.0.1', 12345);
      _socket.listen((data) {
        final serverResponse = utf8.decode(data);
        print('Server: $serverResponse');
      });
      print('Connected to server');
    } catch (e) {
      print('Unable to connect: $e');
    }
  }

  void sendMessage(String message) {
    _socket.write(message);
  }

  void close() {
    _socket.close();
  }
}
