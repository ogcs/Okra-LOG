/*
 *     Copyright 2016-2026 TinyZ
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ogcs.log.serlvet.grafana.bean;

import java.util.List;

/**
 * @author TinyZ
 * @date 2016-10-12.
 */
public class GfnQueryParam {

    private int panelId;
    private int maxDataPoints;
    private String interval;
    private String format;
    private GfnTimeDuration range;
    private GfnTimeDuration rangeRaw;
    private List<GfnMetricsQuery> targets;

    public int getPanelId() {
        return panelId;
    }

    public void setPanelId(int panelId) {
        this.panelId = panelId;
    }

    public int getMaxDataPoints() {
        return maxDataPoints;
    }

    public void setMaxDataPoints(int maxDataPoints) {
        this.maxDataPoints = maxDataPoints;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public GfnTimeDuration getRange() {
        return range;
    }

    public void setRange(GfnTimeDuration range) {
        this.range = range;
    }

    public GfnTimeDuration getRangeRaw() {
        return rangeRaw;
    }

    public void setRangeRaw(GfnTimeDuration rangeRaw) {
        this.rangeRaw = rangeRaw;
    }

    public List<GfnMetricsQuery> getTargets() {
        return targets;
    }

    public void setTargets(List<GfnMetricsQuery> targets) {
        this.targets = targets;
    }
}
