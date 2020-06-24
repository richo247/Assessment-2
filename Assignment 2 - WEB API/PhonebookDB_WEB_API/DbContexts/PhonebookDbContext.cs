using Microsoft.EntityFrameworkCore;
using PhonebookDB_WEB_API.Models;

namespace PhonebookDB_WEB_API.DbContexts
{
    public class PhonebookDbContext:DbContext
    {
        public PhonebookDbContext(DbContextOptions<PhonebookDbContext> options)
            : base(options)
        { }
        public DbSet<Contact> Contact { get; set; }
        


        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // specify table names
            modelBuilder.Entity<Contact>().ToTable("Contact");

            modelBuilder.Entity<Contact>().HasKey(Contact => Contact.id);
        }
    }

    
}
