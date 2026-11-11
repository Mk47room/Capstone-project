
import { Link, useNavigate, useParams } from "react-router"

function PatientNavbar() {

    const navigate = useNavigate()
    const { token } = useParams()

    const logout = () => {
        localStorage.clear()
        navigate("/")
    }

    return (

        <nav className="navbar navbar-expand-lg bg-white shadow-sm rounded-3 px-3 py-2">
            <Link className="navbar-brand fw-bold text-primary d-flex align-items-center" to={`/patient/${token}`}>
                <i className="bi bi-heart-pulse me-2 fs-4"></i>
                CareAssist
            </Link>
            <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#patientNavbar" aria-controls="patientNavbar"
                aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>

            <div className="collapse navbar-collapse" id="patientNavbar">

                {/* LEFT SIDE */}
                <ul className="navbar-nav me-auto mt-2 mt-lg-0">
                    <li className="nav-item">
                        <Link className="nav-link" to={`/patient/${token}`}>
                            <i className="bi bi-house me-1"></i>
                            Home
                        </Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link" to={`/patient/${token}/planlist`}>
                            <i className="bi bi-shield-check me-1"></i>
                            View Plans
                        </Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link" to={`/patient/${token}/policy`}>
                            <i className="bi bi-file-earmark-text me-1"></i>
                            My Policies
                        </Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link" to={`/patient/${token}/claim`}>
                            <i className="bi bi-clipboard2-pulse me-1"></i>
                            My Claims
                        </Link>
                    </li>
                </ul>

                {/* RIGHT SIDE */}
                <ul className="navbar-nav align-items-lg-center gap-lg-2">
                    <li className="nav-item">
                        <Link className="nav-link" to={`/patient/${token}/profile`}>
                            <i className="bi bi-person-circle me-1"></i>
                            Profile
                        </Link>
                    </li>
                    <li className="nav-item">
                        <button className="btn btn-outline-danger btn-sm" onClick={logout}>
                            <i className="bi bi-box-arrow-right me-1"></i>
                            Logout
                        </button>
                    </li>
                </ul>
            </div>
        </nav>
    )
}

export default PatientNavbar
