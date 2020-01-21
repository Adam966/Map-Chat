using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MapChatServer.Dtos
{
    public class LocationDetailedDto
    {
        public string Country { get; set; }
        public string Town { get; set; }
        public string PostalCode { get; set; }
        public string Address { get; set; }
        public string Longtitude { get; set; }
        public string Latitude { get; set; }
    }
}
