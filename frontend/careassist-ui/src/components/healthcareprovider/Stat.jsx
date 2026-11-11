
import { useSelector } from "react-redux"
import { STATUS_STYLE } from "../common/claimStatus"

function Stat() {

    const list = useSelector(state => state.providerClaims)

    const totalCount = list.length

    const submitCount = list.filter(c => c.status === "SUBMITTED").length

    const approvedCount = list.filter(c => c.status === "APPROVED").length

    const invoicedCount = list.filter(c => c.status === "INVOICED").length

    const rejectedCount = list.filter(c => c.status === "REJECTED").length

    const paidCount = list.filter(c => c.status === "PAID").length

    const cards = [
        {
            label: "Total Claims",
            count: totalCount,
            color: "secondary",
            icon: "bi-clipboard-data"
        },
        {
            label: STATUS_STYLE.SUBMITTED.label,
            count: submitCount,
            color: `${STATUS_STYLE.SUBMITTED.color}`,
            icon: STATUS_STYLE.SUBMITTED.icon
        },
        {
            label: STATUS_STYLE.APPROVED.label,
            count: approvedCount,
            color: `${STATUS_STYLE.APPROVED.color}`,
            icon: STATUS_STYLE.APPROVED.icon
        },
        {
            label: STATUS_STYLE.INVOICED.label,
            count: invoicedCount,
            color: `${STATUS_STYLE.INVOICED.color}`,
            icon: STATUS_STYLE.INVOICED.icon
        },
        {
            label: STATUS_STYLE.REJECTED.label,
            count: rejectedCount,
            color: `${STATUS_STYLE.REJECTED.color}`,
            icon: STATUS_STYLE.REJECTED.icon
        },
        {
            label: STATUS_STYLE.PAID.label,
            count: paidCount,
            color: `${STATUS_STYLE.PAID.color}`,
            icon: STATUS_STYLE.PAID.icon
        }
    ]

    return (
        <div className="row g-3 mb-4">

            {cards.map((card, index) => (
                <div className="col-6 col-md-4" key={index}>
                    <div className="card border-0 shadow-sm h-100">
                        <div className="card-body d-flex align-items-center">

                            <div className={`rounded-circle bg-${card.color}-subtle text-${card.color}-emphasis d-flex p-3 me-3`}>
                                <i className={`bi ${card.icon} fs-4 lh-1`}></i>
                            </div>

                            <div>
                                <div className="fs-3 fw-bold lh-1">
                                    {card.count}
                                </div>
                                <div className="text-muted small">
                                    {card.label}
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            ))}

        </div>
    )
}

export default Stat
