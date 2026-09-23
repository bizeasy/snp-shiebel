import { describe, expect, it } from 'vitest';

import { detectPreviewKind } from './preview-kind';

describe('detectPreviewKind', () => {
  it('recognises a pdf by mime type even with a misleading extension', () => {
    expect(detectPreviewKind('scan.bin', 'application/pdf')).toBe('pdf');
  });

  it('recognises a pdf by extension when the mime type is generic', () => {
    expect(detectPreviewKind('datasheet.pdf', 'application/octet-stream')).toBe('pdf');
  });

  it('recognises a modern docx', () => {
    expect(detectPreviewKind('install-notes.docx', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document')).toBe('docx');
  });

  it('recognises a docx by extension when the browser sent a generic mime type', () => {
    expect(detectPreviewKind('notes.docx', 'application/octet-stream')).toBe('docx');
  });

  it('does not confuse legacy .doc with modern .docx - mammoth cannot read the binary format', () => {
    expect(detectPreviewKind('old-manual.doc', 'application/msword')).toBe('legacy-doc');
    expect(detectPreviewKind('old-manual.doc', null)).toBe('legacy-doc');
  });

  it('recognises common text formats', () => {
    for (const name of ['log.txt', 'data.csv', 'config.json', 'readme.md']) {
      expect(detectPreviewKind(name, null)).toBe('text');
    }
  });

  it('recognises images', () => {
    expect(detectPreviewKind('photo.jpg', 'image/jpeg')).toBe('image');
    expect(detectPreviewKind('diagram.png', null)).toBe('image');
  });

  it('falls back to unsupported for a spreadsheet or archive', () => {
    expect(detectPreviewKind('budget.xlsx', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')).toBe('unsupported');
    expect(detectPreviewKind('archive.zip', 'application/zip')).toBe('unsupported');
  });

  it('handles missing name and mime type without throwing', () => {
    expect(detectPreviewKind(null, null)).toBe('unsupported');
    expect(detectPreviewKind(undefined, undefined)).toBe('unsupported');
  });
});
