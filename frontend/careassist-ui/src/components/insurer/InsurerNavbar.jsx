import { Link, useNavigate, useParams } from "react-router"

function InsurerNavbar() {

    const navigate = useNavigate()
    const { token } = useParams()

    const logout = () => {
        localStorage.clear()
        navigate("/")
    }

    return (
        <nav className="navbar navbar-expand-lg bg-white shadow-sm rounded-3 px-3 py-2">

            <Link className="navbar-brand fw-bold text-primary d-flex align-items-center" to={`/insurer/${token}`}>
                <i className="bi bi-heart-pulse me-2 fs-4"></i>
                Insurer Dashboard
            </Link>

            <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#providerNavbar"
                aria-controls="providerNavbar" aria-expanded="false" aria-label="Toggle navigation" >
                <span className="navbar-toggler-icon"></span>
            </button>

            <div className="collapse navbar-collapse" id="providerNavbar">

                {/* LEFT SIDE */}
                <ul className="navbar-nav me-auto mt-2 mt-lg-0">
                    <li className="nav-item">
                        <Link className="nav-link" to={`/insurer/${token}`}>
                            <i className="bi bi-house me-1"></i>Home
                        </Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link" to={`/insurer/${token}/plan`}>
                            <i className="bi bi-receipt me-1"></i>Plans
                        </Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link" to={`/insurer/${token}/claim`}>
                            <i className="bi bi-receipt me-1"></i>Claim List
                        </Link>
                    </li>
                </ul>

                {/* RIGHT SIDE */}
                <ul className="navbar-nav align-items-lg-center gap-lg-2">
                    <li className="nav-item">
                        <button
                            className="btn btn-outline-danger btn-sm"
                            onClick={logout}>
                            <i className="bi bi-box-arrow-right me-1"></i>Logout
                        </button>
                    </li>
                </ul>
            </div>
        </nav>
    )
}

export default InsurerNavbar