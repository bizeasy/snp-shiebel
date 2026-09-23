/**
 * What kind of in-app viewer a file gets, decided from its name and the mime type the
 * browser reported on upload.
 *
 * Neither signal is trusted alone: a CLI upload without an explicit content-type comes back
 * as application/octet-stream, and a renamed file has the wrong extension. Checking both and
 * preferring whichever one is specific enough to answer is more reliable than either on its
 * own - the same lesson as the mismatched-field bugs found earlier in this port.
 */
export type PreviewKind = 'pdf' | 'image' | 'text' | 'docx' | 'legacy-doc' | 'unsupported';

const TEXT_EXTENSIONS = ['txt', 'csv', 'tsv', 'log', 'md', 'markdown', 'json', 'xml', 'yaml', 'yml', 'ini', 'conf', 'sql'];
const IMAGE_EXTENSIONS = ['png', 'jpg', 'jpeg', 'gif', 'webp', 'bmp', 'svg'];

const DOCX_MIME = 'application/vnd.openxmlformats-officedocument.wordprocessingml.document';
/** application/msword and its .dot/.docm cousins: legacy binary OLE format, not XML. */
const LEGACY_DOC_MIME = ['application/msword', 'application/vnd.ms-word'];
const LEGACY_DOC_EXTENSIONS = ['doc', 'dot'];

export function detectPreviewKind(name: string | null | undefined, mimeType: string | null | undefined): PreviewKind {
  const ext = (name ?? '').split('.').pop()?.toLowerCase() ?? '';
  const mime = (mimeType ?? '').toLowerCase().split(';')[0].trim();

  if (mime === 'application/pdf' || ext === 'pdf') {
    return 'pdf';
  }
  if (mime.startsWith('image/') || IMAGE_EXTENSIONS.includes(ext)) {
    return 'image';
  }
  if (mime === DOCX_MIME || ext === 'docx') {
    return 'docx';
  }
  // Checked after docx: a .docx that arrived with a generic octet-stream mime type still
  // has the right extension, but a bare "application/msword" mime with no extension must
  // not be mistaken for the modern format mammoth can actually read.
  if (LEGACY_DOC_MIME.includes(mime) || LEGACY_DOC_EXTENSIONS.includes(ext)) {
    return 'legacy-doc';
  }
  if (mime.startsWith('text/') || TEXT_EXTENSIONS.includes(ext)) {
    return 'text';
  }
  return 'unsupported';
}
