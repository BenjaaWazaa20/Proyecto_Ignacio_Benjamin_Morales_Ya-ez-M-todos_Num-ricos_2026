// Numérica — micro interactions
document.addEventListener('DOMContentLoaded', () => {
  // Active nav detection
  const path = window.location.pathname;
  document.querySelectorAll('.nav-pill .nav-link').forEach(a=>{
    const href = a.getAttribute('href');
    if(href && href !== '/' && path.startsWith(href)) a.classList.add('active');
    if(href === '/' && path === '/') a.classList.add('active');
  });

  // Animate cards on scroll
  const obs = new IntersectionObserver((entries)=>{
    entries.forEach(e=>{
      if(e.isIntersecting){ e.target.classList.add('animate-in'); obs.unobserve(e.target); }
    });
  },{threshold:.12});
  document.querySelectorAll('.card-numerica, .form-card, .hero, .table-numerica').forEach(el=>obs.observe(el));

  // Auto-dismiss alerts after 6s
  setTimeout(()=>{
    document.querySelectorAll('.alert-dismissible').forEach(el=>{
      try{ bootstrap.Alert.getOrCreateInstance(el).close(); }catch{}
    });
  }, 6000);
});
