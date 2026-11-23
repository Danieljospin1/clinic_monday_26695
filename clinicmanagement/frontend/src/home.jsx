import { useNavigate } from "react-router-dom";

function Home() {
  const navigate = useNavigate();

  return (
    <div>
      <h1>Clinic Management System</h1>

      <button onClick={() => navigate("/create-office")}>
        Create New Office
      </button>

      <br />
      <br />

      <button onClick={() => navigate("/all-offices")}>
        View All Offices
      </button>
    </div>
  );
}

export default Home;