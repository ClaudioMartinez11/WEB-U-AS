package com.citas.reserva;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class ExcelExporter {
    private final Path exportPath;

    public ExcelExporter(@Value("${app.export.path}") String exportPath) { this.exportPath = Path.of(exportPath); }

    public synchronized void exportar(List<Reserva> reservas) {
        try {
            Files.createDirectories(exportPath.getParent() == null ? Path.of(".") : exportPath.getParent());
            try (XSSFWorkbook workbook = crearLibro(reservas); OutputStream output = Files.newOutputStream(exportPath)) {
                workbook.write(output);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo exportar el archivo Excel", exception);
        }
    }

    private XSSFWorkbook crearLibro(List<Reserva> reservas) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reservas");
        String[] columns = {"ID", "Fecha", "Hora", "Nombre", "Telefono", "Creado en"};
        Row header = sheet.createRow(0);
        for (int index = 0; index < columns.length; index++) header.createCell(index).setCellValue(columns[index]);
        for (int index = 0; index < reservas.size(); index++) {
            Reserva reserva = reservas.get(index);
            Row row = sheet.createRow(index + 1);
            row.createCell(0).setCellValue(reserva.id());
            row.createCell(1).setCellValue(reserva.fecha().toString());
            row.createCell(2).setCellValue(reserva.hora().toString());
            row.createCell(3).setCellValue(reserva.nombre());
            row.createCell(4).setCellValue(reserva.telefono());
            row.createCell(5).setCellValue(reserva.creadoEn().toString());
        }
        return workbook;
    }
}