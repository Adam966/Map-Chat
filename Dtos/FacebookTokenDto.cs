
namespace MapChatServer.Dtos
{
    public class FacebookTokenDto
    {
        public string appID { get; set; }

        public string userID { get; set; }

        public bool isValid { get; set; }

        public string facebookToken { get; set; }
    }
}
