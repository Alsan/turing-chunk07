<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- FIXME: FOR-555. This might not be the best solution though, but it sure works -->
  <xsl:template match="comment()|processing-instruction()">
    <xsl:copy>
      <xsl:apply-templates select="@*|*|text()|processing-instruction()|comment()"/>
    </xsl:copy>
  </xsl:template>
<!-- End fixme FOR-555 -->
  <xsl:template match="*">
<!-- remove element prefix (if any) -->
    <xsl:element name="{local-name()}">
<!-- process attributes -->
      <xsl:for-each select="@*">
<!-- remove attribute prefix (if any) -->
        <xsl:attribute name="{local-name()}">
          <xsl:value-of select="."/>
        </xsl:attribute>
      </xsl:for-each>
      <xsl:apply-templates/>
    </xsl:element>
  </xsl:template>
</xsl:stylesheet>
