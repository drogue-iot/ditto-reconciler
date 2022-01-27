{{/*
Encodes Kafka properties from "foo.bar.baz" to "FOO_BAR_BAZ"
Arguments (dict):
  * properties(dict) - the properties to encode
  * prefix(string) - the value to prefix all env-var names with
*/}}
{{- define "ditto-reconciler.translate-kafka-properties" -}}
{{- if .properties -}}
{{- range $key, $value := .properties }}
- name: {{ .prefix }}{{ $key | upper | replace "." "_" }}
  value: {{ $value | quote }}
{{- end }}
{{- end -}}
{{- end }}