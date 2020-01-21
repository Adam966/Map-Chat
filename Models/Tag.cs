using System.Collections.Generic;

namespace MapChatServer.Models
{
    public partial class Tag
    {
        public Tag()
        {
            Eventtag = new HashSet<Eventtag>();
        }

        public int Id { get; set; }
        public string TagText { get; set; }

        public virtual ICollection<Eventtag> Eventtag { get; set; }
    }
}
