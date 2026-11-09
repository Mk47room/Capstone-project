import { Link } from "react-router"
import ObjectList from "./ObjectList"

function Navbar() {
    return (
        <div className="container mt-3">
            <div className="d-flex justify-content-between align-items-center">
                <h2 className="mb-0">Dashboard</h2>
                <Link to="/add" className="btn btn-primary">+ Add New Object</Link>
            </div>

            <ObjectList />
        </div>
    )
}

export default Navbar