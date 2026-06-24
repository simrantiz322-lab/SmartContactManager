console.log("Js added;")

const toggleSidebar = () => {
  const sidebar = document.querySelector(".sidebar");
  const content = document.querySelector(".content");

  if (!sidebar || !content) return;

  const isOpen = sidebar.classList.contains("open") || window.getComputedStyle(sidebar).display !== "none";

  if (isOpen) {
    sidebar.style.animation = "fadeIn 0.2s ease reverse";
    setTimeout(() => {
      sidebar.classList.remove("open");
      sidebar.style.display = "none";
      sidebar.style.animation = "";
      content.style.marginLeft = "0%";
    }, 180);
  } else {
    sidebar.style.display = "block";
    sidebar.classList.add("open");
    sidebar.style.animation = "fadeIn 0.25s ease both";
    content.style.marginLeft = "20%";
  }
};

document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll(".btn-action").forEach((btn) => {
    btn.addEventListener("mouseenter", () => {
      btn.style.transition = "transform 0.35s cubic-bezier(0.34, 1.56, 0.64, 1), box-shadow 0.25s ease";
    });
  });
});