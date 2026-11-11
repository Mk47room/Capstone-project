import axios from "axios"
import { useEffect, useRef, useState } from "react"
import { API_SERVER_URL_DEV } from "../../../config"

function PatientClaim() {

    const [page, setPage] = useState(0)
    const [size, setSize] = useState(5)
    const [totalPages, setTotalPages] = useState(0)
    const [totalRecords, setTotalRecords] = useState(0)
    const [currentPageSize, setCurrentPageSize] = useState(0)

    const [errMsg, setErrMsg] = useState("")
    const [errComment, setErrComment] = useState("")

    const [list, setList] = useState([])
    const [claimType, setClaimType] = useState("")
    const [claimStatus, setClaimStatus] = useState("")
    const [fromDate, setFromDate] = useState("")
    const [toDate, setToDate] = useState("")
    const [policyId, setPolicyId] = useState("")
    const [hospitalName, setHospitalName] = useState("")
    const [diagnosis, setDiagnosis] = useState("")
    const [treatment, setTreatment] = useState("")
    const [admissionDate, setAdmissionDate] = useState("")
    const [dischargeDate, setDischargeDate] = useState("")
    const [claimedAmount, setClaimedAmount] = useState("")

    const [createdClaimId, setCreatedClaimId] = useState(null)
    const [isClaimCreated, setIsClaimCreated] = useState(false)
    const [claimFile, setClaimFile] = useState(null)
    const [isUploaded, setIsUploaded] = useState(false)
    const [uploadMsg, setUploadMsg] = useState("")
    const [docType, setDocType] = useState("")

    const closeBtnRef = useRef(null)

    useEffect(() => {
        const setClaims = async () => {
            try {
                const response = await axios.get(`${API_SERVER_URL_DEV}/api/patient/claim/all?claimType=${claimType}&status=${claimStatus}&fromDate=${fromDate}&toDate=${toDate}&page=${page}&size=${size}`, {
                    headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
                })
                setList(response.data.list)
                setTotalPages(response.data.paginationInfo.totalPages)
                setTotalRecords(response.data.paginationInfo.totalRecords)
                setCurrentPageSize(response.data.paginationInfo.numRecordsInPage)

            } catch (err) {
                setErrMsg(err?.response?.data?.message)
                setErrComment(err?.response?.data?.comment)
            }
        }

        setClaims()

    }, [claimType, claimStatus, fromDate, toDate, page, size])
    const onAddClaim = async (e) => {
        e.preventDefault()
        try {
            setErrMsg("")
            setErrComment("")
            let obj = {
                "hospitalName": hospitalName,
                "diagnosis": diagnosis,
                "treatment": treatment,
                "admissionDate": admissionDate,
                "dischargeDate": dischargeDate,
                "claimedAmount": claimedAmount
            }

            const response = await axios.post(`${API_SERVER_URL_DEV}/api/claim/add/${policyId}`, obj, {
                headers: { Authorization: 'Bearer ' + localStorage.getItem('token') }
            });
            const newClaimId = response.data.claimId
            setCreatedClaimId(newClaimId);
            setIsClaimCreated(true);
            alert("Claim details saved successfully! Now please upload your supporting document.")

        } catch (err) {
            setErrMsg(err?.response?.data?.message)
            setErrComment(err?.response?.data?.comment)
        }
    }

    const handleUploadDoc = async () => {
        if (!claimFile) {
            alert("Please select a file to upload first!")
            return
        }
        if (!docType) {
            alert("Please select or enter a Document Type!")
            return
        }

        const formData = new FormData()
        formData.append("file", claimFile)
        formData.append("docType", docType)

        try {
            setErrMsg("")
            setErrComment("")
            await axios.post(`${API_SERVER_URL_DEV}/api/claim/documents/${createdClaimId}`, formData, {
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token')
                }
            })
            setIsUploaded(true)
            setUploadMsg("Document uploaded successfully!")
            alert("Claim and document successfully submitted!")

            if (closeBtnRef.current) {
                closeBtnRef.current.click()
            }

        } catch (err) {
            setErrMsg(err?.response?.data?.message || "File upload failed")
            setErrComment(err?.response?.data?.comment || "Please check the file and try again.")
        }
    }

    const onPageChange = (val) => {
        setPage(val)
    }

    return (
        <div>
            <div className="card border-0 shadow-sm">
                <div className="card-header bg-white d-flex justify-content-between align-items-center py-3">
                    <h5 className="mb-0">My Claim List</h5>
                    <button className="btn btn-primary" onClick={() => {
                        setHospitalName(""), setErrMsg(""), setErrComment(""),
                            setDiagnosis(""), setTreatment(""), setClaimedAmount("")
                        setPolicyId("")
                        setClaimFile(null)
                        setCreatedClaimId(null)
                        setIsClaimCreated(false)
                        setIsUploaded(false)
                        setUploadMsg("")
                    }} data-bs-toggle="modal" data-bs-target="#claimModal">+ New Claim</button>
                </div>

                {/* <!-- Error / Header --> */}
                <div className="px-3">
                    <p className="text-danger fw-semibold mb-0">
                        {errMsg}
                    </p>
                    <p className="text-muted small mb-0">
                        {errComment}
                    </p>
                </div>

                <div className="table-responsive">
                    <table className="table table-hover align-middle mb-0">
                        <thead className="table-light">
                            <tr>
                                <th>S.No</th>
                                <th>Claim ID</th>
                                <th>Claim Type</th>
                                <th className="text-end">Claimed Amount (₹)</th>
                                <th className="text-end">Approved Amount (₹)</th>
                                <th>Status</th>
                                <th>Diagnosis</th>
                                <th>Submitted Date</th>
                                <th>Hospital Name</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th>Filter Options</th>
                                <th></th>
                                <th scope="col">
                                    <select className="form-control" onChange={(e) => setClaimType(e.target.value)}>
                                        <option value="">--select Type--</option>
                                        <option>CASHLESS</option>
                                        <option>REIMBURSEMENT</option>
                                    </select>
                                </th>
                                <th></th>
                                <th></th>
                                <th scope="col">
                                    <select className="form-control" onChange={(e) => setClaimStatus(e.target.value)}>
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
                                        <input type="date" onChange={(e) => setFromDate(e.target.value)} className="form-control" />
                                    </div>
                                    <div className="row">
                                        <input type="date" onChange={(e) => setToDate(e.target.value)} className="form-control" />
                                    </div>
                                </th>
                                <th> </th>
                            </tr>
                            {list?.map((c, index) => (
                                <tr key={index}>
                                    <th>{index + 1}</th>
                                    <td>{c.claimId}</td>
                                    <td>{c.claimType}</td>
                                    <td className="text-end">{c.claimedAmount}</td>
                                    <td className="text-end">{c.approvedAmount}</td>
                                    <td>{c.status}</td>
                                    <td>{c.diagnosis}</td>
                                    <td>{c.submittedDate.split("T")[0]}</td>
                                    <td>{c.hospitalName}</td>
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
                </div>
            </div>

            <div className="modal fade" id="claimModal" data-bs-backdrop="static" data-bs-keyboard="false"
                tabIndex="-1" aria-labelledby="claimModalLabel" aria-hidden="true">
                <div className="modal-dialog modal-dialog-centered">
                    <div className="modal-content border-0 shadow">
                        <form onSubmit={onAddClaim}>
                            <div className="modal-header">
                                <h5 className="modal-title" id="claimModalLabel">
                                    New Reimbursement Claim {!isClaimCreated ? "(Step 1: Details)" : "(Step 2: Upload Doc)"}
                                </h5>
                                <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close" ref={closeBtnRef} />
                            </div>
                            <div className="modal-body">
                                <div className="mb-3">
                                    <label className="form-label">Policy ID</label>
                                    <input type="number" value={policyId} onChange={(e) => setPolicyId(e.target.value)} className="form-control" required disabled={isClaimCreated} />
                                    <div className="form-text">Your Policy id.</div>
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Hospital Name</label>
                                    <input type="text" value={hospitalName} onChange={(e) => setHospitalName(e.target.value)} className="form-control" required disabled={isClaimCreated} />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Diagnosis</label>
                                    <input type="text" value={diagnosis} onChange={(e) => setDiagnosis(e.target.value)} className="form-control" required disabled={isClaimCreated} />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Treatment</label>
                                    <textarea value={treatment} onChange={(e) => setTreatment(e.target.value)} className="form-control" required disabled={isClaimCreated} />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Admission Date</label>
                                    <input type="date" value={admissionDate} onChange={(e) => setAdmissionDate(e.target.value)} className="form-control" required disabled={isClaimCreated} />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Discharge Date</label>
                                    <input type="date" value={dischargeDate} onChange={(e) => setDischargeDate(e.target.value)} className="form-control" required disabled={isClaimCreated} />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Claimed Amount</label>
                                    <input type="number" value={claimedAmount} onChange={(e) => setClaimedAmount(e.target.value)} className="form-control" required disabled={isClaimCreated} />
                                </div>
                                {isClaimCreated && (
                                    <>
                                        <hr className="my-3" />
                                        <div className="mb-3">
                                            <label className="form-label fw-bold text-success">
                                                Claim Created (ID: {createdClaimId}). Now Upload Document:
                                            </label>
                                            <div className="mb-2">
                                                <label className="form-label small">Document Type</label>
                                                <select
                                                    className="form-control form-control-sm"
                                                    value={docType}
                                                    onChange={(e) => setDocType(e.target.value)}
                                                    required
                                                >
                                                    <option value="">-- Select Doc Type --</option>
                                                    <option value="BILL">BILL</option>
                                                    <option value="PRESCRIPTION">PRESCRIPTION</option>
                                                    <option value="MEDICAL_REPORT">MEDICAL_REPORT</option>
                                                </select>
                                            </div>

                                            <div className="input-group">
                                                <input
                                                    type="file"
                                                    className="form-control"
                                                    onChange={(e) => setClaimFile(e.target.files[0])}
                                                    disabled={isUploaded}
                                                />
                                                <button
                                                    type="button"
                                                    className="btn btn-success"
                                                    onClick={handleUploadDoc}
                                                    disabled={isUploaded || !claimFile || !docType}
                                                >
                                                    {isUploaded ? "Uploaded ✓" : "Upload Doc"}
                                                </button>
                                            </div>
                                            {uploadMsg && <div className="form-text text-success mt-1">{uploadMsg}</div>}
                                        </div>
                                    </>
                                )}
                            </div>

                            <div className="modal-footer">
                                <button type="button" className="btn btn-light" data-bs-dismiss="modal">
                                    Close
                                </button>
                                {!isClaimCreated && (
                                    <button type="submit" className="btn btn-primary">
                                        Save Claim Details
                                    </button>
                                )}
                            </div>
                        </form>

                    </div>
                </div>
            </div>

        </div>
    )
}

export default PatientClaim