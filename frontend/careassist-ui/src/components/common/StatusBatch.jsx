import { STATUS_STYLE } from "./claimStatus"


function StatusBadge({ status }) {
    const s = STATUS_STYLE[status]
    return (
        <span className={`badge rounded-pill text-bg-${s ? s.color : "secondary"}`}>
            {s ? s.label : status}
        </span>
    )
}

export default StatusBadge