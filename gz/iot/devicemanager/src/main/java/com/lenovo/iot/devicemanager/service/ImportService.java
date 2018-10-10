package com.lenovo.iot.devicemanager.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.lenovo.iot.devicemanager.model.DeviceImport;

public class ImportService {
	private class ResultInfo {
		public List<DeviceImport> device_list = new ArrayList<DeviceImport>();
		public List<DeviceImport> error_device_list = new ArrayList<DeviceImport>();
	}

	public JSONObject checkImportDevice(InputStream stream) {
		boolean result;
		String error = "";

		JSONObject obj = new JSONObject();

		try {
			HSSFWorkbook wb = new HSSFWorkbook(stream);
			if (!checkExcelField(wb)) {
				result = false;
				error = "模板文件标题列不能被修改或者删除，请重新下载模版";
			} else {
				ResultInfo excelInfo = parseXls(wb);
				if (excelInfo.device_list.size() == 0 && excelInfo.error_device_list.size() == 0) {
					result = false;
					error = "模版中没有设备信息，请向模板中添加后重新上传。";
				} else if (excelInfo.device_list.size() + excelInfo.error_device_list.size() > 200) {
					result = false;
					error = "每次导入设备总数不能超过200，请修改模版并重新上传。";
				} else if (excelInfo.error_device_list.size() > 0) {
					result = false;
					error = "模版存在错误，请检查后重新上传。";
					obj.put("rows", excelInfo.error_device_list);
				} else if(checkIsRepeat(excelInfo.device_list)){
					result = false;
					error = "设备名称中存在重复值，请检查后重新上传。";
					obj.put("rows", excelInfo.device_list);
				} else {
					result = true;
					obj.put("rows", excelInfo.device_list);
				}
			}
		} catch (IOException e) {
			result = false;
			error = e.getMessage();

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		obj.put("result", result);
		obj.put("error", error);

		return obj;
	}

	private boolean checkExcelField(HSSFWorkbook wb) {
		try {
			HSSFSheet sheet = wb.getSheetAt(0); // 获得第一个表单
			Iterator<Row> rows = sheet.rowIterator(); // 获得第一个表单的迭代器
			int i = 0;
			while (rows.hasNext()) {
				Row row = rows.next(); // 获得行数据
				i++;
				// if (i++ < 2) {
				// continue;
				// }

				if (i == 1) {
					Iterator<Cell> cells = row.cellIterator(); // 获得第一行的迭代器
					while (cells.hasNext()) {
						Cell cell = cells.next();
						switch (cell.getCellType()) { // 根据cell中的类型来输出数据
						case HSSFCell.CELL_TYPE_STRING:
							String fieldname = cell.getStringCellValue().trim();
							if (cell.getColumnIndex() == 0) {
								if (!fieldname.equals("网关设备接入名称")) {
									return false;
								}
							} else if (cell.getColumnIndex() == 1) {
								if (!fieldname.equals("网关设备描述")) {
									return false;
								}
							}
							break;
						}
					}
					return true;
				}
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	private ResultInfo parseXls(HSSFWorkbook wb) {
		ResultInfo ret = new ResultInfo();

		try {
			HSSFSheet sheet = wb.getSheetAt(0); // 获得第一个表单
			Iterator<Row> rows = sheet.rowIterator(); // 获得第一个表单的迭代器
			while (rows.hasNext()) {
				Row row = rows.next(); // 获得行数据
				if (row.getRowNum() < 1) {
					continue;
				}

				DeviceImport device = new DeviceImport();
				Iterator<Cell> cells = row.cellIterator(); // 获得第一行的迭代器
				while (cells.hasNext()) {
					Cell cell = cells.next();
					String fieldvalue = getCellValue(cell);
					if (cell.getColumnIndex() == 0) {
						device.setAccess_key(fieldvalue);
					} else if (cell.getColumnIndex() == 1) {
						device.setDevice_desc(fieldvalue);
					}
				}

				if (device.getAccess_key().isEmpty()) {
					device.setError(true);
					device.setError_message("设备名称为空");
				} else if (device.getAccess_key().length() < 4 || device.getAccess_key().length() > 18) {
					device.setError(true);
					device.setError_message("设备名称长度要求是4-18个字符");
				}

				device.setRow(row.getRowNum());

				if (device.isError()) {
					ret.error_device_list.add(device);
				} else {
					ret.device_list.add(device);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return ret;
	}

	private String getCellValue(Cell cell) {
		if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			return String.valueOf((long) cell.getNumericCellValue());
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			return cell.getStringCellValue().trim();
		}
		return "";
	}

	private boolean checkIsRepeat(List<DeviceImport> list) {
		boolean flag = false;
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = i + 1; j < list.size(); j++) {
				// 如果相等，则重复
				if (list.get(i).getAccess_key().equalsIgnoreCase(list.get(j).getAccess_key())) {
					flag = true;
					
					list.get(j).setError(true);
					list.get(j).setError_message("名称重复");
				}
			}
		}

		return flag;
	}
}
