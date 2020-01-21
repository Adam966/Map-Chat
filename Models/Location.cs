using System.Collections.Generic;

namespace MapChatServer.Models
{
    public partial class Location
    {
        public Location()
        {
            Eventgroup = new HashSet<Eventgroup>();
        }

        public int Id { get; set; }
        public string Country { get; set; }
        public string Town { get; set; }
        public string PostalCode { get; set; }
        public string Address { get; set; }
        public string Longtitude { get; set; }
        public string Latitude { get; set; }

        public virtual ICollection<Eventgroup> Eventgroup { get; set; }
    }
}
