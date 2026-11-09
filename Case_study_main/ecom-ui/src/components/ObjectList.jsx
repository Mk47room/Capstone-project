import axios from "axios";
import { useEffect, useState } from "react";

function ObjectList() {
    const [list, setList] = useState([]);
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const [totalPages, setTotalPages] = useState(0);


    useEffect(() => {
        const getObject = async () => {
            try {
                const response = await axios.get("https://api.restful-api.dev/objects")
                
                setList(response.data);
                setTotalPages(Math.ceil(response.data.length / size));
            } catch (err) {
                console.log(err);
            }
        };

        getObject();
    }, [size]);

    const onDelete = async (e, id) => {
        e.preventDefault();
        try {
            await axios.delete(`https://api.restful-api.dev/objects/${id}`);
            const updatedList = list.filter(item => item.id !== id);
            setList(updatedList);
            setTotalPages(Math.ceil(updatedList.length / size));
        } catch (err) {
            console.log(err);
        }
    };

    const onPageChange = (val) => {
        setPage(val);
    };

    const item = list.slice(page * size, (page + 1) * size);

    return (
        <div className="container mt-4">
            <h2 className="mb-4">Object List</h2>

            <div className="row">
                {item.map((o,index) => (
                    <div className="col-md-4 mb-3" key={index}>
                        <div className="card h-100 shadow-sm">
                            <div className="card-body d-flex flex-column justify-content-between">
                                <div>
                                    <h4 className="card-title">{o.name}</h4>
                                    <ul className="list-group list-group-flush mb-3">
                                        <li className="list-group-item">
                                            <strong>Color:</strong> {o.data?.color || o.data?.["Staple color"] || "Null"}
                                        </li>
                                        <li className="list-group-item">
                                            <strong>Price:</strong> {o.data?.price ? "Rs. " + o.data.price : o.data?.["Price"] || "Null"}
                                        </li>
                                        <li className="list-group-item">
                                            <strong>CPU:</strong> {o.data?.cpu || o.data?.["CPU model"] || "Null"}
                                        </li>
                                    </ul>
                                </div>
                                <button className="btn btn-danger btn-sm align-self-start" onClick={(e) => onDelete(e, o.id)}>
                                    Delete
                                </button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            {totalPages > 1 && (
                <nav aria-label="Page navigation example" className="mt-4">
                    <ul className="pagination justify-content-center">
                        <li className={`page-item ${page === 0 ? 'disabled' : ''}`}>
                            <button className="page-link" onClick={() => setPage(page - 1)} disabled={page === 0}>
                                Previous
                            </button>
                        </li>

                        {Array.from({ length: totalPages }, (_, index) => (
                            <li key={index} className={`page-item ${page === index ? 'active' : ''}`}>
                                <button className="page-link" onClick={() => onPageChange(index)}>
                                    {index + 1}
                                </button>
                            </li>
                        ))}

                        <li className={`page-item ${page === totalPages - 1 ? 'disabled' : ''}`}>
                            <button className="page-link" onClick={() => setPage(page + 1)} disabled={page === totalPages - 1}>
                                Next
                            </button>
                        </li>
                    </ul>
                </nav>
            )}
        </div>
    );
}

export default ObjectList;