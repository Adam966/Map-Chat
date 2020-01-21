
namespace MapChatServer.Dtos
{
    public class UserAuthDto
    {
        public int id { get; set; }
        public string FacebookId { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string facebookToken { get; set; }
    }
}
