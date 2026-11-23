
import { useState, useEffect } from "react";
import axios from "axios";

function Offices() {

    const [offices, setOffices] = useState([]);
    const [search, setSearch] = useState("");
    const [loading, setLoading] = useState(true);
    const [deletingId, setDeletingId] = useState(null);

    // Fetch all free offices when the component mounts
    useEffect(() => {

        const fetchOffices = async () => {
            try {
                const response = await axios.get(
                    "http://localhost:1014/api/offices/free"
                );

                setOffices(response.data);

            } catch (error) {
                console.error("Error fetching offices:", error);
                alert("Failed to fetch offices!");
            } finally {
                setLoading(false);
            }
        };

        fetchOffices();

    }, []);

    // Search by office name or office number
    const filteredOffices = offices.filter((office) => {

        return (
            office.name.toLowerCase().includes(search.toLowerCase()) ||
            String(office.officeNumber).includes(search)
        );

    });

    // Delete office by ID
    const handleDelete = async (id) => {

        const confirmDelete = window.confirm(
            "Are you sure you want to delete this office?"
        );

        if (!confirmDelete) return;

        try {
            setDeletingId(id);

            await axios.delete(
                `http://localhost:1014/api/offices/${id}`
            );

            // Remove deleted office from the displayed list
            setOffices((previousOffices) =>
                previousOffices.filter((office) => office.id !== id)
            );

            alert("Office deleted successfully!");

        } catch (error) {
            console.error("Error deleting office:", error);
            alert("Failed to delete office!");
        } finally {
            setDeletingId(null);
        }
    };

    return (
        <div>

            <h2>Available Offices</h2>

            {/* Search input */}
            <div>
                <label>Search Office: </label>

                <input
                    type="text"
                    placeholder="Search by name or office number"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />
            </div>

            <br />

            {/* Offices table */}
            {loading ? (
                <p>Loading offices...</p>
            ) : (
                <table border="1" cellPadding="10">

                    <thead>
                        <tr>
                            <th>Office Name</th>
                            <th>Office Number</th>
                            <th>Action</th>
                        </tr>
                    </thead>

                    <tbody>

                        {filteredOffices.length > 0 ? (

                            filteredOffices.map((office) => (

                                <tr key={office.id}>

                                    <td>{office.name}</td>

                                    <td>{office.officeNumber}</td>

                                    <td>
                                        <button
                                            onClick={() => handleDelete(office.id)}
                                            disabled={deletingId === office.id}
                                        >
                                            {deletingId === office.id
                                                ? "Deleting..."
                                                : "Delete"}
                                        </button>

                                        {" "}

                                        <button disabled>
                                            Update
                                        </button>
                                    </td>

                                </tr>

                            ))

                        ) : (

                            <tr>
                                <td colSpan="3">
                                    No offices found.
                                </td>
                            </tr>

                        )}

                    </tbody>

                </table>
            )}

        </div>
    );
}

export default Offices;
