
import { useEffect, useState } from "react"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"
import { STATUS_STYLE } from "../common/claimStatus"

function InsurerClaimChart() {

    const [claims, setClaims] = useState([])
    const [error, setError] = useState("")

    useEffect(() => {

        const getClaims = async () => {
            try {
                const response = await axios.get(
                    `${API_SERVER_URL_DEV}/api/claim/filter?page=0&size=50`,
                    {
                        headers: {
                            Authorization: "Bearer " + localStorage.getItem("token")
                        }
                    }
                )

                setClaims(response.data.list)

            } catch (err) {
                console.log(err)
                setError("Unable to load claim statistics")
            }
        }

        getClaims()

    }, [])

    const statusData = [
        {
            name: STATUS_STYLE.SUBMITTED.label,
            count: claims.filter(c => c.status === "SUBMITTED").length,
            color: "bg-warning"
        },
        {
            name: STATUS_STYLE.APPROVED.label,
            count: claims.filter(c => c.status === "APPROVED").length,
            color: "bg-info"
        },
        {
            name: STATUS_STYLE.INVOICED.label,
            count: claims.filter(c => c.status === "INVOICED").length,
            color: "bg-primary"
        },
        {
            name: STATUS_STYLE.REJECTED.label,
            count: claims.filter(c => c.status === "REJECTED").length,
            color: "bg-danger"
        },
        {
            name: STATUS_STYLE.PAID.label,
            count: claims.filter(c => c.status === "PAID").length,
            color: "bg-success"
        }
    ]

    const maxCount = Math.max(...statusData.map(s => s.count), 1)

    return (
        <div className="card border-0 shadow-sm mb-4">
            <div className="card-body">

                <h5 className="fw-bold mb-1">Claims Overview</h5>

                <p className="text-muted small mb-4">
                    Number of claims by status
                </p>

                {error ? (
                    <div className="text-danger">{error}</div>
                ) : (

                    <div className="row align-items-end text-center">

                        {statusData.map((status, index) => (
                            <div className="col" key={index}>

                                <div
                                    className="d-flex flex-column justify-content-end align-items-center"
                                    style={{ height: "200px" }}
                                >

                                    <span className="fw-bold mb-2">
                                        {status.count}
                                    </span>

                                    <div
                                        className={`w-75 rounded-top ${status.color}`}
                                        style={{
                                            height: `${(status.count / maxCount) * 140}px`,
                                            minHeight: status.count > 0 ? "8px" : "0px",
                                            transition: "height 0.3s"
                                        }}
                                    />

                                </div>

                                <div className="small text-muted mt-2">
                                    {status.name}
                                </div>

                            </div>
                        ))}

                    </div>

                )}

            </div>
        </div>
    )
}

export default InsurerClaimChart
