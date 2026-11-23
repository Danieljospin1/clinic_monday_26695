import { useState } from "react";
import axios from "axios";

function Office() {

    const [name, setName] = useState("");
    const [officeNumber, setOfficeNumber] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        const office = {
            name: name,
            officeNumber: Number(officeNumber)
        };

        try {
            const response = await axios.post(
                "http://localhost:1014/api/offices/save",
                office
            );

            alert("Office created successfully!");

            console.log(response.data);

            setName("");
            setOfficeNumber("");

        } catch (error) {
            console.error(error);
            alert("Office created successfully!");
        }
    };

    return (
        <div>
            <h2>Register Office</h2>

            <form onSubmit={handleSubmit}>

                <div>
                    <label>Office Name:</label>
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>

                <br />

                <div>
                    <label>Office Number:</label>
                    <input
                        type="number"
                        value={officeNumber}
                        onChange={(e) => setOfficeNumber(e.target.value)}
                        required
                    />
                </div>

                <br />

                <button type="submit">
                    Save Office
                </button>

            </form>
        </div>
    );
}

export default Office;