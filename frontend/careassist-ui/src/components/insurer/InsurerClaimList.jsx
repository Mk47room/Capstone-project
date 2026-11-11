import axios from "axios"
import { use, useEffect, useRef, useState } from "react"
import { API_SERVER_URL_DEV } from "../../../config"

function InsurerClaimList() {

    const [page, setPage] = useState(0)
    const [size, setSize] = useState(5)
    const [totalPages, setTotalPages] = useState(0)
    const [totalRecords, setTotalRecords] = useState(0)
    const [currentPageSize, setCurrentPageSize] = useState(0)

    const [list, setList] = useState([])
    const [selectedClaim, setSelectedClaim] = useState(null)
    const [claimId, setClaimId] = useState("")
    const [claimType, setClaimType] = useState("")
    const [claimStatus, setClaimStatus] = useState("")
    const [fromDate, setFromDate] = useState("")
    const [toDate, setToDate] = useState("")
    const [policyId, setPolicyId] = useState("")
    const [hospitalName, setHospitalName] = useState("")
    const [diagnosis, setDiagnosis] = useState("")
    const [treatment, setTreatment] = useState("")
    const [submittedDate, setSubmittedDate] = useState("")
    const [claimedAmount, setClaimedAmount] = useState("")
    const [invoice, setInvoice] = useState(null)
    const [showInvoice, setShowInvoice] = useState(false)
    const [showDocument, setShowDocument] = useState(false)
    const [showRejectModal, setShowRejectModal] = useState(false)
    const [rejectedReason, setRejectedReason] = useState("")
    const [documents, setDocuments] = useState([])


    useEffect(() => {
        const setClaims = async () => {
            try {
                const response = await axios.get(`${API_SERVER_URL_DEV}/api/claim/filter?claimType=${claimType}&status=${claimStatus}&fromDate=${fromDate}&toDate=${toDate}&page=${page}&size=${size}`, {
                    headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
                })
                setList(response.data.list)
                setTotalPages(response.data.paginationInfo.totalPages)
                setTotalRecords(response.data.paginationInfo.totalRecords)
                setCurrentPageSize(response.data.paginationInfo.numRecordsInPage)


            } catch (err) {
                const message = err?.response?.data?.message || "Something went wrong"
                const comment = err?.response?.data?.comment || ""

                alert(message + "\n" + comment)
            }
        }

        setClaims()

    }, [claimType, claimStatus, fromDate, toDate, page, size])

    const onInvoice = async (e, claimId) => {
        e.preventDefault()

        try {
            const response = await axios.get(`${API_SERVER_URL_DEV}/api/claim/invoice/${claimId}`, {
                headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
            })

            setInvoice(response.data)
            setShowInvoice(true)

        } catch (err) {
            const message = err?.response?.data?.message || "Something went wrong"
            const comment = err?.response?.data?.comment || ""
            alert(message + "\n" + comment)
        }
    }

    const onApprove = async (e, claimId) => {
        e.preventDefault()

        try {
            await axios.put(`${API_SERVER_URL_DEV}/api/claim/approve/${claimId}`, {}, {
                headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
            })

            alert("Claim Approved Successfully!")
        } catch (err) {
            const message = err?.response?.data?.message || "Something went wrong"
            const comment = err?.response?.data?.comment || ""
            alert(message + "\n" + comment)
        }
    }

    const onPay = async (e, claimId, approvedAmount) => {
        e.preventDefault()

        try {
            await axios.put(`${API_SERVER_URL_DEV}/api/claim/pay/${claimId}`, {}, {
                headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
            })

            alert("Payment Success " + "\n" + "Rs. " + approvedAmount)
        } catch (err) {
            const message = err?.response?.data?.message || "Something went wrong"
            const comment = err?.response?.data?.comment || ""
            alert(message + "\n" + comment)
        }
    }

    const onReject = async (e, claimId) => {
        e.preventDefault()

        let obj = {
            "rejectedReason": rejectedReason
        }

        try {
            await axios.put(`${API_SERVER_URL_DEV}/api/claim/reject/${claimId}`, obj, {
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token')
                }
            })

            alert("Claim Rejected!!..")
            closeRejectModal()
        } catch (err) {
            const message = err?.response?.data?.message || "Something went wrong"
            const comment = err?.response?.data?.comment || ""
            alert(message + "\n" + comment)
        }
    }

    const onShowDocuments = async (e, claimId) => {
        e.preventDefault()

        try {
            const response = await axios.get(
                `${API_SERVER_URL_DEV}/api/claim/documents/${claimId}`,
                {
                    headers: {
                        Authorization: 'Bearer ' + localStorage.getItem('token')
                    }
                }
            )

            setDocuments(response.data)
            setShowDocument(true)

        } catch (err) {
            const message = err?.response?.data?.message || "Something went wrong"
            const comment = err?.response?.data?.comment || ""
            alert(message + "\n" + comment)
        }
    }

    const onViewDocument = async (documentId) => {
        try {
            const response = await axios.get(
                `${API_SERVER_URL_DEV}/api/claim/documents/download/${documentId}`,
                {
                    headers: {
                        Authorization: 'Bearer ' + localStorage.getItem('token')
                    },
                    responseType: "blob"
                }
            )

            const file = new Blob([response.data], {
                type: response.headers["content-type"]
            })

            const fileUrl = URL.createObjectURL(file)

            const link = document.createElement("a")
            link.href = fileUrl
            link.target = "_blank"
            link.rel = "noopener noreferrer"
            link.click()

            setTimeout(() => URL.revokeObjectURL(fileUrl), 60000)

        } catch (err) {
            alert("Unable to view document")
        }
    }

    const openRejectModal = () => {
        setRejectedReason("")
        setShowRejectModal(true)
    }

    const closeRejectModal = () => {
        setShowRejectModal(false)
        setRejectedReason("")
    }

    const onPageChange = (val) => {
        setPage(val)
    }

    return (
        <div>
            <div className="card border-0 shadow-sm">

                <div className="card-header bg-white d-flex justify-content-between align-items-center py-3">
                    <h5 className="mb-0">Total Claim List</h5>
                </div>

                <div className="table-responsive">
                    <table className="table table-hover align-middle mb-0">
                        <thead className="table-light">
                            <tr>
                                <th>S.No</th>
                                <th>Claim ID</th>
                                <th>Claim Type</th>
                                <th>Status</th>
                                <th>Diagnosis</th>
                                <th>Submitted Date</th>
                                <th className="text-end">Claimed Amount (₹)</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th>Filter Options</th>
                                <th></th>
                                <th scope="col">
                                    <select className="form-control" onChange={(e) => { setClaimType(e.target.value), setPage(0), setSelectedClaim(null) }}>
                                        <option value="">--select Type--</option>
                                        <option>CASHLESS</option>
                                        <option>REIMBURSEMENT</option>
                                    </select>
                                </th>
                                <th scope="col">
                                    <select className="form-control" onChange={(e) => { setClaimStatus(e.target.value), setPage(0), setSelectedClaim(null) }}>
                                        <option value="">--select Status--</option>
                                        <option>SUBMITTED</option>
                                        <option>APPROVED</option>
                                        <option>INVOICED</option>
                                        <option>REJECTED</option>
                                        <option>PAID</option>
                                    </select>
                                </th>
                                <th>
                                    <div className="row">
                                        <label>From : </label>
                                    </div>
                                    <div className="row">
                                        <label>To : </label>
                                    </div>
                                </th>
                                <th>
                                    <div className="row">
                                        <input type="date" onChange={(e) => { setFromDate(e.target.value), setPage(0), setSelectedClaim(null) }} className="form-control" />
                                    </div>
                                    <div className="row">
                                        <input type="date" onChange={(e) => { setToDate(e.target.value), setPage(0), setSelectedClaim(null) }} className="form-control" />
                                    </div>
                                </th>
                                <th> </th>
                                <th> </th>
                            </tr>
                            {list?.map((c, index) => (
                                <tr key={index}>
                                    <th>{index + 1}</th>
                                    <td>{c.claimId}</td>
                                    <td>{c.claimType}</td>
                                    <td>{c.status}</td>
                                    <td>{c.diagnosis}</td>
                                    <td>{c.submittedDate.split("T")[0]}</td>
                                    <td className="text-end">{c.claimedAmount}</td>
                                    <td>
                                        <button
                                            className="btn btn-secondary"
                                            onClick={() => {
                                                setSelectedClaim(c)
                                                setInvoice(null)
                                                setShowInvoice(false)
                                                setShowDocument(false)
                                            }}
                                        >View</button>
                                    </td>
                                </tr>
                            ))}

                        </tbody>
                    </table>
                    <nav aria-label="Page navigation example" style={{ "alignContent": 'center' }}>
                        <ul className="pagination justify-content-center">
                            <li className="page-item"><button className="page-link" onClick={() => setPage(page - 1)}
                                disabled={page === 0}>Previous</button></li>

                            {
                                Array.from({ length: totalPages }, (e, index) => (
                                    <li key={index} className="page-item">
                                        <button className="page-link" onClick={() => onPageChange(index)}>{index + 1}</button></li>
                                ))}

                            <li className="page-item"><button className="page-link" onClick={() => setPage(page + 1)}
                                disabled={page === (totalPages - 1)} >Next</button></li>

                        </ul>
                        <p>{`Page ${page + 1} of ${totalPages} total pages`}</p>
                        <p>{`Showing ${currentPageSize} records from total ${totalRecords} records`}</p>
                    </nav>
                    {selectedClaim && (
                        <div className="card border-0 shadow-sm m-3">

                            <div className="card-header bg-primary-subtle d-flex justify-content-between align-items-center">
                                <h5 className="mb-0 fw-bold">
                                    Claim Details - #{selectedClaim.claimId}
                                </h5>

                                <button
                                    className="btn-close"
                                    onClick={() => {
                                        setSelectedClaim(null)
                                        setInvoice(null)
                                        setShowInvoice(false)
                                        setShowDocument(false)
                                    }}
                                ></button>
                            </div>

                            <div className="card-body">

                                <div className="row g-3">

                                    <div className="col-md-4">
                                        <label className="text-muted small">Claim ID</label>
                                        <div className="fw-semibold">{selectedClaim.claimId}</div>
                                    </div>

                                    <div className="col-md-4">
                                        <label className="text-muted small">Patient Name</label>
                                        <div className="fw-semibold">{selectedClaim.patientName}</div>
                                    </div>

                                    <div className="col-md-4">
                                        <label className="text-muted small">Policy ID</label>
                                        <div className="fw-semibold">{selectedClaim.policyId}</div>
                                    </div>

                                    <div className="col-md-4">
                                        <label className="text-muted small">Claim Type</label>
                                        <div className="fw-semibold">{selectedClaim.claimType}</div>
                                    </div>

                                    <div className="col-md-4">
                                        <label className="text-muted small">Status</label>
                                        <div className="fw-semibold">{selectedClaim.status}</div>
                                    </div>

                                    <div className="col-md-4">
                                        <label className="text-muted small">Risk Category</label>
                                        <div className="fw-semibold">{selectedClaim.riskCategory}</div>
                                    </div>

                                    <div className="col-md-6">
                                        <label className="text-muted small">Hospital Name</label>
                                        <div className="fw-semibold">{selectedClaim.hospitalName}</div>
                                    </div>

                                    <div className="col-md-6">
                                        <label className="text-muted small">Submitted Date</label>
                                        <div className="fw-semibold">
                                            {selectedClaim.submittedDate?.split("T")[0]}
                                        </div>
                                    </div>

                                    <div className="col-md-6">
                                        <label className="text-muted small">Diagnosis</label>
                                        <div className="fw-semibold">{selectedClaim.diagnosis}</div>
                                    </div>

                                    <div className="col-md-6">
                                        <label className="text-muted small">Treatment</label>
                                        <div className="fw-semibold">{selectedClaim.treatment}</div>
                                    </div>

                                    <div className="col-md-4">
                                        <label className="text-muted small">Claimed Amount</label>
                                        <div className="fw-semibold">₹{selectedClaim.claimedAmount}</div>
                                    </div>

                                    <div className="col-md-4">
                                        <label className="text-muted small">Approved Amount</label>
                                        <div className="fw-semibold">₹{selectedClaim.approvedAmount}</div>
                                    </div>

                                    {selectedClaim.rejectedReason && (
                                        <div className="col-md-12">
                                            <label className="text-muted small">Rejection Reason</label>
                                            <div className="text-danger fw-semibold">
                                                {selectedClaim.rejectedReason}
                                            </div>
                                        </div>
                                    )}

                                </div>
                                <div className="d-flex justify-content-end gap-2 mt-4">

                                    {/* CASHLESS + SUBMITTED */}
                                    {selectedClaim.status === "SUBMITTED" &&
                                        selectedClaim.claimType === "CASHLESS" && (
                                            <>
                                                <button className="btn btn-success" onClick={(e) => onApprove(e, selectedClaim.claimId)}>
                                                    Approve
                                                </button>

                                                <button className="btn btn-danger" onClick={openRejectModal}>
                                                    Reject
                                                </button>
                                            </>
                                        )}

                                    {/* REIMBURSEMENT + SUBMITTED */}
                                    {selectedClaim.status === "SUBMITTED" &&
                                        selectedClaim.claimType === "REIMBURSEMENT" && (
                                            <>
                                                <button className="btn btn-success" onClick={(e) => onApprove(e, selectedClaim.claimId)}>
                                                    Approve
                                                </button>

                                                <button className="btn btn-danger" onClick={openRejectModal}>
                                                    Reject
                                                </button>
                                            </>
                                        )}

                                    {/* REIMBURSEMENT + APPROVED */}
                                    {selectedClaim.status === "APPROVED" &&
                                        selectedClaim.claimType === "REIMBURSEMENT" && (
                                            <button
                                                className="btn btn-secondary"
                                                onClick={(e) => onShowDocuments(e, selectedClaim.claimId)}
                                            >
                                                See Doc
                                            </button>
                                        )}

                                    {/* INVOICED */}
                                    {selectedClaim.status === "INVOICED" && (
                                        <button
                                            className="btn btn-info"
                                            onClick={(e) => onInvoice(e, selectedClaim.claimId)}
                                        >
                                            See Invoice
                                        </button>
                                    )}

                                </div>

                            </div>
                        </div>
                    )}
                    {showDocument &&
                        selectedClaim?.status === "APPROVED" &&
                        selectedClaim?.claimType === "REIMBURSEMENT" && (
                            <div className="card border-0 shadow-sm m-3">

                                <div className="card-header bg-warning-subtle d-flex justify-content-between align-items-center">
                                    <h5 className="mb-0 fw-bold">
                                        Claim Document - #{selectedClaim.claimId}
                                    </h5>

                                    <button
                                        className="btn-close"
                                        onClick={() => setShowDocument(false)}
                                    ></button>
                                </div>

                                <div className="card-body">

                                    {/* Display the reimbursement document here */}

                                    <div className="text-center p-5 border rounded">
                                        {documents.length > 0 ? (
                                            <div className="table-responsive">
                                                <table className="table table-bordered table-hover align-middle">
                                                    <thead className="table-light">
                                                        <tr>
                                                            <th>Document Type</th>
                                                            <th>File Name</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>

                                                    <tbody>
                                                        {documents.map((doc) => (
                                                            <tr key={doc.documentId}>
                                                                <td>{doc.docType}</td>
                                                                <td>{doc.fileName}</td>
                                                                <td>
                                                                    <button
                                                                        className="btn btn-sm btn-primary"
                                                                        onClick={() => onViewDocument(doc.documentId)}
                                                                    >
                                                                        <i className="bi bi-eye"></i> View Doc
                                                                    </button>
                                                                </td>
                                                            </tr>
                                                        ))}
                                                    </tbody>
                                                </table>
                                            </div>
                                        ) : (
                                            <p className="text-muted">No documents uploaded for this claim.</p>
                                        )}
                                    </div>

                                    <div className="d-flex justify-content-end gap-2 mt-4">

                                        <button className="btn btn-primary" onClick={(e) => onPay(e, selectedClaim.claimId, selectedClaim.approvedAmount)}>
                                            Pay
                                        </button>

                                        <button className="btn btn-danger" onClick={openRejectModal}>
                                            Reject
                                        </button>

                                    </div>

                                </div>
                            </div>
                        )}
                    {showInvoice && invoice && selectedClaim?.status === "INVOICED" && (
                        <div className="card border-0 shadow-sm m-3">

                            <div className="card-header bg-info-subtle d-flex justify-content-between align-items-center">
                                <h5 className="mb-0 fw-bold">
                                    Invoice Details - #{invoice.claimId}
                                </h5>

                                <button
                                    className="btn-close"
                                    onClick={() => {
                                        setShowInvoice(false)
                                        setInvoice(null)
                                    }}
                                ></button>
                            </div>

                            <div className="card-body">

                                <div className="row g-3">

                                    <div className="col-md-4">
                                        <label className="text-muted small">Claim ID</label>
                                        <div className="fw-semibold">{invoice.claimId}</div>
                                    </div>

                                    <div className="col-md-4">
                                        <label className="text-muted small">Invoice Date</label>
                                        <div className="fw-semibold">
                                            {invoice.invoiceDate?.split("T")[0]}
                                        </div>
                                    </div>

                                    <div className="col-md-4">
                                        <label className="text-muted small">Diagnosis</label>
                                        <div className="fw-semibold">{invoice.diagnosis}</div>
                                    </div>

                                    <div className="col-md-4">
                                        <label className="text-muted small">Consultation Fee</label>
                                        <div className="fw-semibold">₹{invoice.consultationFee}</div>
                                    </div>

                                    <div className="col-md-4">
                                        <label className="text-muted small">Diagnostics Fee</label>
                                        <div className="fw-semibold">₹{invoice.diagnosticsFee}</div>
                                    </div>

                                    <div className="col-md-4">
                                        <label className="text-muted small">Pharmacy Fee</label>
                                        <div className="fw-semibold">₹{invoice.pharmacyFee}</div>
                                    </div>

                                    <div className="col-md-4">
                                        <label className="text-muted small">Room Charges</label>
                                        <div className="fw-semibold">₹{invoice.roomCharges}</div>
                                    </div>

                                    <div className="col-md-4">
                                        <label className="text-muted small">Tax</label>
                                        <div className="fw-semibold">₹{invoice.tax}</div>
                                    </div>

                                    <div className="col-md-4">
                                        <label className="text-muted small">Total Amount</label>
                                        <div className="fw-bold text-primary">₹{invoice.totalAmount}</div>
                                    </div>

                                    <div className="col-md-4">
                                        <label className="text-muted small">Claimed Amount</label>
                                        <div className="fw-semibold">₹{invoice.claimedAmount}</div>
                                    </div>

                                    <div className="col-md-4">
                                        <label className="text-muted small">Approved Amount</label>
                                        <div className="fw-semibold">₹{invoice.approvedAmount}</div>
                                    </div>

                                </div>

                                <div className="d-flex justify-content-end gap-2 mt-4">

                                    <button className="btn btn-primary" onClick={(e) => onPay(e, selectedClaim.claimId, selectedClaim.approvedAmount)}>
                                        Pay
                                    </button>

                                    <button className="btn btn-danger" onClick={openRejectModal}>
                                        Reject
                                    </button>

                                </div>

                            </div>
                        </div>
                    )}

                </div>
            </div>
            {showRejectModal && (
                <>
                    {/* Modal backdrop */}
                    <div
                        className="modal-backdrop fade show"
                        style={{ zIndex: 1050 }}
                    ></div>

                    {/* Reject Reason Modal */}
                    <div
                        className="modal fade show d-block"
                        tabIndex="-1"
                        role="dialog"
                        style={{ zIndex: 1055 }}
                    >
                        <div className="modal-dialog modal-dialog-centered">
                            <div className="modal-content">

                                <div className="modal-header">
                                    <h5 className="modal-title">
                                        Reject Claim
                                    </h5>

                                    <button
                                        type="button"
                                        className="btn-close"
                                        onClick={closeRejectModal}
                                    ></button>
                                </div>

                                <div className="modal-body">
                                    <label className="form-label fw-semibold">
                                        Rejected Reason
                                    </label>

                                    <textarea
                                        className="form-control"
                                        rows="4"
                                        placeholder="Enter the reason for rejecting this claim..."
                                        value={rejectedReason}
                                        onChange={(e) => setRejectedReason(e.target.value)}
                                    ></textarea>

                                    {!rejectedReason.trim() && (
                                        <small className="text-danger">
                                            Rejection reason is required.
                                        </small>
                                    )}
                                </div>

                                <div className="modal-footer">
                                    <button
                                        type="button"
                                        className="btn btn-secondary"
                                        onClick={closeRejectModal}
                                    >
                                        Back
                                    </button>

                                    <button
                                        type="button"
                                        className="btn btn-danger"
                                        disabled={!rejectedReason.trim()}
                                        onClick={(e) => onReject(e, selectedClaim.claimId)}
                                    >
                                        Reject
                                    </button>
                                </div>

                            </div>
                        </div>
                    </div>
                </>
            )}

        </div>
    )
}

export default InsurerClaimList