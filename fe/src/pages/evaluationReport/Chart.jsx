import React, { useEffect, useState } from "react";
import { PieChart, Pie, Tooltip, Cell, Legend } from "recharts";
import "./Chart.css";
import { chart } from "~/services/ReportService";

const Chart = ({ filters }) => {
  const [scoreChart, setScoreChart] = useState({});
  const [statusCounts, setStatusCounts] = useState({});

  const translateScore = (key) => {
    switch (key) {
      case "scoreA":
        return "≥ 8"; // lớn hơn hoặc bằng 8
      case "scoreB":
        return "6.5 – 8"; // từ 6.5 đến 8
      case "scoreC":
        return "< 6.5"; // nhỏ hơn 6.5
      default:
        return key;
    }
  };

  const STATUS_COLORS = {
    COMPLETED: "#3b82f6", // xanh dương
    ACTIVE: "#10b981", // xanh lá
    DROPPED: "#ef4444", // đỏ
    SUSPENDED: "#f59e0b", // cam
  };

  const SCORE_COLORS = {
    scoreA: "#6366f1", // indigo
    scoreB: "#06b6d4", // cyan
    scoreC: "#f97316", // orange
  };

  const fetchChart = async () => {
    const data = await chart({
      universityId: filters.universityId,
      internshipProgramId: filters.internshipProgramId,
    });
    setScoreChart(data.scoreChart);
    setStatusCounts(data.statusCounts);
    console.log(data.scoreChart);
    console.log(data.statusCounts);
  };

  useEffect(() => {
    fetchChart();
  }, [filters]);

  const scoreArray = Object.entries(scoreChart).map(([key, value]) => ({
    name: translateScore(key),
    value: value,
    color: SCORE_COLORS[key] || "#6b7280",
  }));

  const statusArray = Object.entries(statusCounts).map(([key, value]) => ({
    name: key.toLocaleLowerCase(),
    value: value,
    color: STATUS_COLORS[key] || "#6b7280",
  }));

  return (
    <div className="chart-container">
      <div className="chart-box">
        <div>
          <h2>Biểu đồ điểm</h2>
          <PieChart width={500} height={300}>
            <Pie
              data={scoreArray}
              dataKey="value"
              cx="50%"
              cy="50%"
              outerRadius={100}
              label={false}
              stroke="none"
            >
              {scoreArray.map((entry, index) => (
                <Cell key={index} fill={entry.color} />
              ))}
            </Pie>
            <Tooltip />
            <Legend layout="horizontal" verticalAlign="bottom" align="center" />
          </PieChart>
        </div>
      </div>

      <div className="chart-box">
        <div>
          <h2>Biểu đồ trạng thái</h2>
          <PieChart width={500} height={300}>
            <Pie
              data={statusArray}
              dataKey="value"
              cx="50%"
              cy="50%"
              outerRadius={100}
              label={false}
              stroke="none"
            >
              {statusArray.map((entry, index) => (
                <Cell key={index} fill={entry.color} />
              ))}
            </Pie>
            <Tooltip />
            <Legend layout="horizontal" verticalAlign="bottom" align="center" />
          </PieChart>
        </div>
      </div>
    </div>
  );
};

export default Chart;
