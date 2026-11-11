
import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router"
import { STATUS_STYLE } from "../common/claimStatus"
import axios from "axios"
import { API_SERVER_URL_DEV } from "../../../config"

function InsurerStat() {

    const [page, setPage] = useState(0)
    const [size, setSize] = useState(50)
    const [errMsg, setErrMsg] = useState("")
    const [errComment, setErrComment] = useState("")

    const [totalClaims, setTotalClaims] = useState(0)
    const [submittedClaims, setSubmittedClaims] = useState(0)
    const [approvedClaims, setApprovedClaims] = useState(0)
    const [invoicedClaims, setInvoicedClaims] = useState(0)
    const [rejectedClaims, setRejectedClaims] = useState(0)
    const [paidClaims, setPaidClaims] = useState(0)

    const params = useParams()
    const navigate = useNavigate()

    useEffect(() => {

        const urlToken = params.token
        const memToken = localStorage.getItem("token")

        if (urlToken !== memToken) {
            navigate("/no-access")
            return
        }

        const getUserDetails = async () => {
            try {
                const resp = await axios.get(API_SERVER_URL_DEV + "/api/auth/user-details", {
                    headers: { Authorization: "Bearer " + memToken }
                })

                if (resp.data.role !== "INSURER") {
                    navigate("/no-access")
                }
            } catch (err) {
                navigate("/no-access")
            }
        }

        const getStat = async () => {
            try {
                const response = await axios.get(`${API_SERVER_URL_DEV}/api/claim/filter?page=${page}&size=${size}`, {
                    headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
                })
                
                const claims = response.data.list
                // console.log(claims)
                setTotalClaims(claims.length)
                setSubmittedClaims(claims.filter(c => c.status === "SUBMITTED").length)
                setApprovedClaims(claims.filter(c => c.status === "APPROVED").length)
                setInvoicedClaims(claims.filter(c => c.status === "INVOICED").length)
                setRejectedClaims(claims.filter(c => c.status === "REJECTED").length)
                setPaidClaims(claims.filter(c => c.status === "PAID").length)

            } catch (err) {
                console.log(err)
                setErrMsg(err?.response?.data?.message)
                setErrComment(err?.response?.data?.comment)
            }
        }
        getUserDetails()
        getStat()
    }, [params.token, navigate, page, size])


    const statCards = [
        {
            title: "Total Claims",
            count: totalClaims,
            color: "primary",
            icon: "bi-clipboard2-pulse"
        },
        {
            title: STATUS_STYLE.SUBMITTED.label,
            count: submittedClaims,
            color: STATUS_STYLE.SUBMITTED.color,
            icon: STATUS_STYLE.SUBMITTED.icon
        },
        {
            title: STATUS_STYLE.APPROVED.label,
            count: approvedClaims,
            color: STATUS_STYLE.APPROVED.color,
            icon: STATUS_STYLE.APPROVED.icon
        },
        {
            title: STATUS_STYLE.INVOICED.label,
            count: invoicedClaims,
            color: STATUS_STYLE.INVOICED.color,
            icon: STATUS_STYLE.INVOICED.icon
        },
        {
            title: STATUS_STYLE.REJECTED.label,
            count: rejectedClaims,
            color: STATUS_STYLE.REJECTED.color,
            icon: STATUS_STYLE.REJECTED.icon
        },
        {
            title: STATUS_STYLE.PAID.label,
            count: paidClaims,
            color: STATUS_STYLE.PAID.color,
            icon: STATUS_STYLE.PAID.icon
        }
    ]


    return (
        <div className="row g-3 mb-4">
            {statCards.map((stat, index) => (
                <div className="col-6 col-md-4" key={index}>
                    <div className="card border-0 shadow-sm h-100">
                        <div className="card-body d-flex align-items-center">
                            <div className={`rounded-circle bg-${stat.color}-subtle text-${stat.color}-emphasis d-flex p-3 me-3`}>
                                <i className={`bi ${stat.icon} fs-4 lh-1`}></i>
                            </div>
                            <div>
                                <div className="fs-3 fw-bold lh-1">
                                    {stat.count}
                                </div>
                                <div className="text-muted small">
                                    {stat.title}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    )
}

export default InsurerStat
