
namespace MapChatServer.Helpers
{
    public interface ICipherService
    {
       string Encrypt(string input);
       string Decrypt(string cipherText);
    }
}
