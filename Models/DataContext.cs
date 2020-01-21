using Microsoft.EntityFrameworkCore;

namespace MapChatServer.Models
{
    public partial class DataContext : DbContext
    {
        public DataContext(DbContextOptions<DataContext> options) : base(options)
        {
            
        }

        public virtual DbSet<Eventgroup> Eventgroup { get; set; }
        public virtual DbSet<Eventgroupuser> Eventgroupuser { get; set; }
        public virtual DbSet<Eventtag> Eventtag { get; set; }
        public virtual DbSet<Friend> Friend { get; set; }
        public virtual DbSet<Location> Location { get; set; }
        public virtual DbSet<Messageeg> Messageeg { get; set; }
        public virtual DbSet<Messageu> Messageu { get; set; }
        public virtual DbSet<Tag> Tag { get; set; }
        public virtual DbSet<Userinfo> Userinfo { get; set; }
        
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
            modelBuilder.Entity<Eventgroup>(entity =>
            {
                entity.ToTable("eventgroup");

                entity.HasIndex(e => e.IdL)
                    .HasName("idL");

                entity.HasIndex(e => e.idU)
                    .HasName("idU");

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .HasColumnType("int(11)");

                entity.Property(e => e.CreationTime)
                    .HasColumnName("creationTime")
                    .HasColumnType("datetime")
                    .HasDefaultValueSql("CURRENT_TIMESTAMP");

                entity.Property(e => e.Description)
                    .HasColumnName("description")
                    .HasColumnType("text")
                    .HasCollation("utf8mb4_0900_ai_ci")
                    .HasCharSet("utf8mb4");

                entity.Property(e => e.GroupName)
                    .HasColumnName("groupName")
                    .HasColumnType("varchar(30)")
                    .HasCollation("utf8mb4_0900_ai_ci")
                    .HasCharSet("utf8mb4");

                entity.Property(e => e.IdL)
                    .HasColumnName("idL")
                    .HasColumnType("int(11)");

                entity.Property(e => e.idU)
                .HasColumnName("idU")
                .HasColumnType("int(11)");

                entity.Property(e => e.MeetTime)
                    .HasColumnName("meetTime")
                    .HasColumnType("datetime");

                entity.Property(e => e.Type)
                    .HasColumnName("type")
                    .HasColumnType("tinyint(4)");

                entity.Property(e => e.active)
                    .HasColumnName("active")
                    .HasColumnType("tinyint(4)");

                entity.HasOne(d => d.IdLNavigation)
                    .WithMany(p => p.Eventgroup)
                    .HasForeignKey(d => d.IdL)
                    .HasConstraintName("eventgroup_ibfk_1");
            });

            modelBuilder.Entity<Eventgroupuser>(entity =>
            {
                entity.ToTable("eventgroupuser");

                entity.HasIndex(e => e.IdU)
                    .HasName("idU");

                entity.HasIndex(e => new { e.IdEg, e.IdU })
                    .HasName("id")
                    .IsUnique();

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .HasColumnType("int(11)");

                entity.Property(e => e.Active)
                    .HasColumnName("active")
                    .HasColumnType("tinyint(4)")
                    .HasDefaultValueSql("'1'");

                entity.Property(e => e.Admin)
                    .HasColumnName("admin")
                    .HasColumnType("tinyint(4)")
                    .HasDefaultValueSql("'0'");

                entity.Property(e => e.IdEg)
                    .HasColumnName("idEG")
                    .HasColumnType("int(11)");

                entity.Property(e => e.IdU)
                    .HasColumnName("idU")
                    .HasColumnType("int(11)");

                entity.HasOne(d => d.IdEgNavigation)
                    .WithMany(p => p.Eventgroupuser)
                    .HasForeignKey(d => d.IdEg)
                    .HasConstraintName("eventgroupuser_ibfk_1");

                entity.HasOne(d => d.IdUNavigation)
                    .WithMany(p => p.Eventgroupuser)
                    .HasForeignKey(d => d.IdU)
                    .HasConstraintName("eventgroupuser_ibfk_2");
            });

            modelBuilder.Entity<Eventtag>(entity =>
            {
                entity.ToTable("eventtag");

                entity.HasIndex(e => e.IdEg)
                    .HasName("idEG");

                entity.HasIndex(e => e.IdT)
                    .HasName("idT");

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .HasColumnType("int(11)");

                entity.Property(e => e.IdEg)
                    .HasColumnName("idEG")
                    .HasColumnType("int(11)");

                entity.Property(e => e.IdT)
                    .HasColumnName("idT")
                    .HasColumnType("int(11)");

                entity.HasOne(d => d.IdEgNavigation)
                    .WithMany(p => p.Eventtag)
                    .HasForeignKey(d => d.IdEg)
                    .HasConstraintName("eventtag_ibfk_1");

                entity.HasOne(d => d.IdTNavigation)
                    .WithMany(p => p.Eventtag)
                    .HasForeignKey(d => d.IdT)
                    .HasConstraintName("eventtag_ibfk_2");
            });

            modelBuilder.Entity<Friend>(entity =>
            {
                entity.ToTable("friend");

                entity.HasIndex(e => e.FriendId)
                    .HasName("friendID");

                entity.HasIndex(e => new { e.UserId, e.FriendId })
                    .HasName("id")
                    .IsUnique();

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .HasColumnType("int(11)");

                entity.Property(e => e.Active)
                    .HasColumnName("active")
                    .HasColumnType("tinyint(4)");

                entity.Property(e => e.FriendId)
                    .HasColumnName("friendID")
                    .HasColumnType("int(11)");

                entity.Property(e => e.UserId)
                    .HasColumnName("userID")
                    .HasColumnType("int(11)");

                entity.HasOne(d => d.FriendNavigation)
                    .WithMany(p => p.FriendFriendNavigation)
                    .HasForeignKey(d => d.FriendId)
                    .HasConstraintName("friend_ibfk_2");

                entity.HasOne(d => d.User)
                    .WithMany(p => p.FriendUser)
                    .HasForeignKey(d => d.UserId)
                    .HasConstraintName("friend_ibfk_1");
            });

            modelBuilder.Entity<Location>(entity =>
            {
                entity.ToTable("location");

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .HasColumnType("int(11)");

                entity.Property(e => e.Address)
                    .HasColumnName("address")
                    .HasColumnType("varchar(50)")
                    .HasCollation("utf8mb4_0900_ai_ci")
                    .HasCharSet("utf8mb4");

                entity.Property(e => e.Country)
                    .HasColumnName("country")
                    .HasColumnType("varchar(50)")
                    .HasCollation("utf8mb4_0900_ai_ci")
                    .HasCharSet("utf8mb4");

                entity.Property(e => e.Latitude)
                    .HasColumnName("latitude")
                    .HasColumnType("varchar(20)")
                    .HasCollation("utf8mb4_0900_ai_ci")
                    .HasCharSet("utf8mb4");

                entity.Property(e => e.Longtitude)
                    .HasColumnName("longtitude")
                    .HasColumnType("varchar(20)")
                    .HasCollation("utf8mb4_0900_ai_ci")
                    .HasCharSet("utf8mb4");

                entity.Property(e => e.PostalCode)
                    .HasColumnName("postalCode")
                    .HasColumnType("varchar(20)")
                    .HasCollation("utf8mb4_0900_ai_ci")
                    .HasCharSet("utf8mb4");

                entity.Property(e => e.Town)
                    .HasColumnName("town")
                    .HasColumnType("varchar(50)")
                    .HasCollation("utf8mb4_0900_ai_ci")
                    .HasCharSet("utf8mb4");
            });

            modelBuilder.Entity<Messageeg>(entity =>
            {
                entity.ToTable("messageeg");

                entity.HasIndex(e => e.IdEg)
                    .HasName("idEG");

                entity.HasIndex(e => e.IdU)
                    .HasName("idU");

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .HasColumnType("int(11)");

                entity.Property(e => e.CTime)
                    .HasColumnName("cTime")
                    .HasColumnType("datetime");

                entity.Property(e => e.IdEg)
                    .HasColumnName("idEG")
                    .HasColumnType("int(11)");

                entity.Property(e => e.IdU)
                    .HasColumnName("idU")
                    .HasColumnType("int(11)");

                entity.Property(e => e.MessageText)
                    .HasColumnName("messageText")
                    .HasColumnType("text")
                    .HasCollation("utf8mb4_0900_ai_ci")
                    .HasCharSet("utf8mb4");

                entity.HasOne(d => d.IdEgNavigation)
                    .WithMany(p => p.Messageeg)
                    .HasForeignKey(d => d.IdEg)
                    .HasConstraintName("messageeg_ibfk_2");

                entity.HasOne(d => d.IdUNavigation)
                    .WithMany(p => p.Messageeg)
                    .HasForeignKey(d => d.IdU)
                    .HasConstraintName("messageeg_ibfk_1");
            });

            modelBuilder.Entity<Messageu>(entity =>
            {
                entity.ToTable("messageu");

                entity.HasIndex(e => e.ReceiverId)
                    .HasName("receiverID");

                entity.HasIndex(e => e.SenderId)
                    .HasName("senderID");

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .HasColumnType("int(11)");

                entity.Property(e => e.CTime)
                    .HasColumnName("cTime")
                    .HasColumnType("datetime");

                entity.Property(e => e.MessageText)
                    .HasColumnName("messageText")
                    .HasColumnType("text")
                    .HasCollation("utf8mb4_0900_ai_ci")
                    .HasCharSet("utf8mb4");

                entity.Property(e => e.ReceiverId)
                    .HasColumnName("receiverID")
                    .HasColumnType("int(11)");

                entity.Property(e => e.SenderId)
                    .HasColumnName("senderID")
                    .HasColumnType("int(11)");

                entity.HasOne(d => d.Receiver)
                    .WithMany(p => p.MessageuReceiver)
                    .HasForeignKey(d => d.ReceiverId)
                    .HasConstraintName("messageu_ibfk_2");

                entity.HasOne(d => d.Sender)
                    .WithMany(p => p.MessageuSender)
                    .HasForeignKey(d => d.SenderId)
                    .HasConstraintName("messageu_ibfk_1");
            });

            modelBuilder.Entity<Tag>(entity =>
            {
                entity.ToTable("tag");

                entity.HasIndex(e => e.TagText)
                    .HasName("tagText")
                    .IsUnique();

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .HasColumnType("int(11)");

                entity.Property(e => e.TagText)
                    .HasColumnName("tagText")
                    .HasColumnType("varchar(30)")
                    .HasCollation("utf8mb4_0900_ai_ci")
                    .HasCharSet("utf8mb4");
            });

            modelBuilder.Entity<Userinfo>(entity =>
            {
                entity.ToTable("userinfo");

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .HasColumnType("int(11)");

                entity.Property(e => e.FacebookId)
                    .IsRequired()
                    .HasColumnName("facebookID")
                    .HasColumnType("varchar(128)")
                    .HasCollation("utf8mb4_0900_ai_ci")
                    .HasCharSet("utf8mb4");

                entity.Property(e => e.FirstName)
                    .IsRequired()
                    .HasColumnName("firstName")
                    .HasColumnType("varchar(30)")
                    .HasCollation("utf8mb4_0900_ai_ci")
                    .HasCharSet("utf8mb4");

                entity.Property(e => e.LastName)
                    .IsRequired()
                    .HasColumnName("lastName")
                    .HasColumnType("varchar(30)")
                    .HasCollation("utf8mb4_0900_ai_ci")
                    .HasCharSet("utf8mb4");

                entity.Property(e => e.LastOnline)
                    .HasColumnName("lastOnline")
                    .HasColumnType("datetime");

                entity.Property(e => e.ProfilePhotoUrl)
                    .HasColumnName("ProfilePhotoUrl")
                    .HasColumnType("tinytext")
                    .HasCollation("utf8mb4_0900_ai_ci")
                    .HasCharSet("utf8mb4");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
        
    }
}
